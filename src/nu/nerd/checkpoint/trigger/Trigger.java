package nu.nerd.checkpoint.trigger;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.action.Action;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class Trigger {

    @SuppressWarnings("unchecked")
    public static final List<Class<? extends Trigger>> triggerTypes = Arrays.asList(BlockTrigger.class, ItemTrigger.class);
    private static Map<String, Class<? extends Trigger>> triggerTypeMap = new HashMap<>();
    static {
        for (Class<? extends Trigger> triggerType : triggerTypes) {
            try {
                triggerTypeMap.put(triggerType.newInstance().getType(), triggerType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Action action;

    /**
     * Execute this trigger's action on the given player.
     * @param player the player
     */
    public void dispatch(CheckpointPlayer player) {
        action.execute(player);
    }

    /**
     * Returns the trigger's type as a string.
     * @return the trigger type
     */
    public abstract String getType();

    /**
     * Returns the trigger's parameters as a string.
     * @return the trigger params string
     */
    public String getParams() {
        return "";
    }

    /**
     * Returns the trigger's action.
     * @return the action
     */
    public Action getAction() {
        return action;
    }

    abstract void loadFromConfig(CheckpointCourse course, Map<String, Object> section) throws CheckpointException;

    abstract void loadFromCommand(CheckpointPlayer player, Queue<String> params) throws CheckpointException;

    abstract void saveToConfig(Map<String, Object> config);

    private void loadActionFromConfig(CheckpointCourse course, Map<String, Object> section) throws CheckpointException {
        Map<String, Object> actionSection = Utils.getSection(section, "action");
        if (actionSection == null) {
            throw new CheckpointException("No action provided");
        }

        try {
            action = Action.deserialize(course, actionSection);
        } catch (CheckpointException e) {
            throw e.prepend("Unable to load action");
        }
    }

    private void loadActionFromCommand(CheckpointPlayer player, Queue<String> params) throws CheckpointException {
        action = Action.deserialize(player, params);
    }

    /**
     * Returns true if this trigger's action references the given checkpoint.
     * @param checkpoint the checkpoint
     * @return whether the checkpoint is referenced
     */
    public boolean referencesCheckpoint(Checkpoint checkpoint) {
        return action.referencesCheckpoint(checkpoint);
    }

    /**
     * Serializes this trigger to a map.
     * @return the serialized trigger
     */
    public Map<String, Object> serialize() {
        Map<String, Object> config = new HashMap<>();
        config.put("type", getType());
        config.put("action", action.serialize());
        saveToConfig(config);
        return config;
    }

    /**
     * Deserializes a trigger from configuration.
     * @param course the course the trigger belongs to
     * @param config the config to deserialize from
     * @return the trigger
     * @throws CheckpointException if the trigger could not be loaded
     */
    public static Trigger deserialize(CheckpointCourse course, Map<String, Object> config) throws CheckpointException {
        String triggerType = Utils.getString(config, "type");
        Trigger trigger = fromType(course, triggerType);
        trigger.loadFromConfig(course, config);
        trigger.loadActionFromConfig(course, config);
        return trigger;
    }

    /**
     * Deserializes a trigger from a command string.
     * @param player the player who ran the command
     * @param params the command parameters specifying the trigger
     * @return the trigger
     * @throws CheckpointException if the trigger could not be loaded
     */
    public static Trigger deserialize(CheckpointPlayer player, Queue<String> params) throws CheckpointException {
        if (params.isEmpty()) {
            throw new UsageException(null);
        }

        String triggerType = params.poll();
        CheckpointCourse course = player.getCourse();
        Trigger trigger = fromType(course, triggerType);
        trigger.loadFromCommand(player, params);
        trigger.loadActionFromCommand(player, params);
        return trigger;
    }

    private static Trigger fromType(CheckpointCourse course, String triggerType) throws CheckpointException {
        Class<? extends Trigger> triggerClass = triggerTypeMap.get(triggerType);
        if (triggerClass == null) {
            throw new CheckpointException("Unrecognized trigger type {{" + triggerType
                    + "}}. Type {{/trigger triggers}} to list available trigger types.");
        }
        try {
            return triggerClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CheckpointException("An internal error occurred");
        }
    }

    @Override
    public String toString() {
        return "{{" + getType() + "}} " + getParams() + " -> " + action.toString();
    }

}
