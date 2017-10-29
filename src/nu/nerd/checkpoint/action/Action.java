package nu.nerd.checkpoint.action;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Abstraction of an action that occurs when a Trigger is activated.
 */
public abstract class Action {

    @SuppressWarnings("unchecked")
    private static List<Class<? extends Action>> actionTypes = Arrays.asList(
            CheckpointAction.class,
            SetCheckpointAction.class,
            GiveItemAction.class,
            OpenIndexAction.class
    );

    private static Map<String, Class<? extends Action>> actionTypeMap = new HashMap<>();
    static {
        for (Class<? extends Action> actionType : actionTypes) {
            try {
                actionTypeMap.put(actionType.newInstance().getType(), actionType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Executes the action for the given player.
     * @param player the player to execute the action for
     */
    public abstract void execute(CheckpointPlayer player);

    /**
     * Returns the type of action.
     * @return the action type
     */
    public abstract String getType();

    /**
     * Returns the action's parameters as a string.
     * @return the action params string
     */
    public String getParams() {
        return "";
    }

    protected String getUsage() {
        return null;
    }

    /**
     * Returns true if this references the given checkpoint.
     * @param checkpoint the checkpoint
     * @return whether the checkpoint is referenced
     */
    public boolean referencesCheckpoint(Checkpoint checkpoint) {
        return false;
    }

    protected abstract void loadFromConfig(CheckpointCourse course, Map<String, Object> section) throws CheckpointException;

    protected abstract void loadFromCommand(CheckpointPlayer player, Queue<String> params) throws CheckpointException;

    protected void saveToConfig(Map<String, Object> config) {
    }

    /**
     * Serializes the action to a map.
     * @return the serialized map
     */
    public Map<String, Object> serialize() {
        Map<String, Object> config = new HashMap<>();
        config.put("type", getType());
        saveToConfig(config);
        return config;
    }

    /**
     * Deserializes an action from a configuration section.
     * @param course the checkpoint course
     * @param section the config to deserialize from
     * @return the action
     * @throws CheckpointException if the action could not be loaded
     */
    public static Action deserialize(CheckpointCourse course, Map<String, Object> section) throws CheckpointException {
        String actionType = Utils.getString(section, "type");
        Action action = fromType(actionType);
        action.loadFromConfig(course, section);
        return action;
    }

    /**
     * Deserializes an action from a command string.
     * @param player the player who ran the command
     * @param params the command parameters specifying the action
     * @return the action
     * @throws CheckpointException if the action could not be loaded
     */
    public static Action deserialize(CheckpointPlayer player, Queue<String> params) throws CheckpointException {
        if (params.isEmpty()) {
            throw new UsageException(null);
        }

        String actionType = params.poll();
        Action action = fromType(actionType);
        try {
            action.loadFromCommand(player, params);
        } catch (UsageException e) {
            String usage = action.getUsage();
            String message = "Usage for action {{" + action.getType() + "}}: {{" + action.getType() + usage + "}}";
            throw new UsageException(null, message);
        }
        return action;
    }

    private static Action fromType(String actionType) throws CheckpointException {
        Class<? extends Action> actionClass = actionTypeMap.get(actionType);
        if (actionClass == null) {
            throw new CheckpointException("Unrecognized action type {{" + actionType
                    + "}}. Type {{/trigger actions}} to list available action types.");
        }
        try {
            return actionClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CheckpointException("An internal error occurred");
        }
    }

    @Override
    public String toString() {
        return "{{" + getType() + "}} " + getParams();
    }

}
