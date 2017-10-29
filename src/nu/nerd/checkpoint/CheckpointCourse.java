package nu.nerd.checkpoint;

import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.trigger.Trigger;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A course containing a set of {@code Checkpoint}s.
 */
public class CheckpointCourse {

    private CheckpointPlugin plugin;
    private List<Checkpoint> checkpoints;
    private Map<String, Checkpoint> checkpointMap;
    private List<Trigger> triggers;
    private String name;

    /**
     * Creates a new {@code CheckpointCourse}.
     *
     * @param name the course's name
     */
    public CheckpointCourse(String name) {
        plugin = CheckpointPlugin.getInstance();
        checkpoints = new ArrayList<>();
        checkpointMap = new HashMap<>();
        triggers = new ArrayList<>();
        this.name = name;
    }

    /**
     * Returns the course's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a {@code Checkpoint} to the course.
     *
     * @param label a label for the {@code Checkpoint}
     * @param location the location that the {@code Checkpoint} teleports the player to
     * @return the {@code Checkpoint} if it was added, or {@code null} if a checkpoint with the same label exists
     * @throws CheckpointException if a checkpoint with the given label already exists in the course
     */
    public Checkpoint addCheckpoint(String label, Location location) throws CheckpointException {
        if (checkpointMap.containsKey(label)) {
            throw new CheckpointException("A checkpoint with label {{" + label + "}} already exists");
        }

        Checkpoint checkpoint = new Checkpoint(this, label, location);
        checkpoints.add(checkpoint);
        checkpointMap.put(label, checkpoint);
        plugin.saveToConfig();
        return checkpoint;
    }

    /**
     * Removes the given {@code Checkpoint} from the course.
     *
     * @param checkpoint the {@code Checkpoint} to remove
     * @return the {@code Checkpoint} that was removed
     * @throws CheckpointException if the checkpoint does not exist in the course
     */
    public Checkpoint removeCheckpoint(Checkpoint checkpoint) throws CheckpointException {
        return removeCheckpoint(checkpoint.getLabel());
    }

    /**
     * Removes the {@code Checkpoint} with the given label from the course.
     *
     * @param label the label of the {@code Checkpoint} to remove
     * @return the {@code Checkpoint} that was removed
     * @throws CheckpointException if no checkpoint with the given label exists in the course
     */
    public Checkpoint removeCheckpoint(String label) throws CheckpointException {
        Checkpoint checkpoint = checkpointMap.remove(label);
        if (checkpoint == null) {
            throw new CheckpointException("No checkpoint with label {{" + label + "}} exists");
        }
        checkpoints.remove(checkpoint);
        for (Iterator<Trigger> it = triggers.iterator(); it.hasNext();) {
            Trigger trigger = it.next();
            if (trigger.referencesCheckpoint(checkpoint)) {
                it.remove();
            }
        }

        plugin.saveToConfig();
        plugin.reindexTriggers();
        return checkpoint;
    }

    /**
     * Reorders the {@code Checkpoint} to the given index in the list.
     *
     * @param checkpoint the {@code Checkpoint} to reorder
     * @param index the new index of the {@code Checkpoint}
     * @throws CheckpointException if the index is invalid
     */
    public void reorderCheckpoint(Checkpoint checkpoint, int index) throws CheckpointException {
        if (index < 0 || index >= checkpoints.size()) {
            throw new CheckpointException("index must be between 0 and " + (checkpoints.size() - 1));
        }

        checkpoints.remove(checkpoint);
        checkpoints.add(index, checkpoint);
        plugin.saveToConfig();
    }

    /**
     * Returns the {@code Checkpoint} with the given label.
     *
     * @param label the label of the {@code Checkpoint} to search for
     * @return the {@code Checkpoint} with the given label
     * @throws CheckpointException if no checkpoint with the given label exists in the course
     */
    public Checkpoint getCheckpoint(String label) throws CheckpointException {
        Checkpoint checkpoint = checkpointMap.get(label);
        if (checkpoint == null) {
            throw new CheckpointException("Unknown checkpoint {{" + label + "}}");
        }
        return checkpoint;
    }

    /**
     * Gets an ordered list of all checkpoints in the course.
     * @return a list of all checkpoints in the course
     */
    public List<Checkpoint> getCheckpoints() {
        return paginateCheckpoints(0, checkpoints.size());
    }

    /**
     * Gets a page of checkpoints as a list.
     * @param startIndex the offset to start with
     * @param limit the number of checkpoints to return
     * @return a list of paginated checkpoints
     */
    public List<Checkpoint> paginateCheckpoints(int startIndex, int limit) {
        int endIndex = Math.min(startIndex + limit, checkpoints.size());
        return checkpoints.subList(startIndex, endIndex);
    }

    /**
     * Returns the number of checkpoints in the course.
     * @return the number of checkpoints
     */
    public int checkpointCount() {
        return checkpoints.size();
    }

    /**
     * Adds the specified trigger to the course.
     * @param trigger the trigger to add
     */
    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
        plugin.saveToConfig();
        plugin.reindexTriggers();
    }

    /**
     * Removes the specified trigger from the course.
     * @param trigger the trigger to remove
     */
    public void removeTrigger(Trigger trigger) {
        triggers.remove(trigger);
        plugin.saveToConfig();
        plugin.reindexTriggers();
    }

    /**
     * Returns the trigger at the specified index.
     * @param index the index to check
     * @return the trigger
     * @throws CheckpointException if the index is invalid
     */
    public Trigger getTrigger(int index) throws CheckpointException {
        if (index < 0 || index >= triggers.size()) {
            throw new CheckpointException("index must be between 0 and " + (checkpoints.size() - 1));
        }
        return triggers.get(index);
    }

    /**
     * Returns a list of triggers in the course.
     * @return the triggers
     */
    public List<Trigger> getTriggers() {
        return triggers;
    }

    /**
     * Serializes the {@code CheckpointCourse} to a {@code Map}.
     *
     * @return the serialized {@code Map}
     */
    Map<String, Object> serialize() {
        Map<String, Object> config = new HashMap<>();

        List<Map<String, Object>> checkpointsConfig = new ArrayList<>();
        for (Checkpoint checkpoint : checkpoints) {
            checkpointsConfig.add(checkpoint.serialize());
        }
        config.put("checkpoints", checkpointsConfig);

        List<Map<String, Object>> triggersConfig = new ArrayList<>();
        for (Trigger trigger : triggers) {
            triggersConfig.add(trigger.serialize());
        }
        config.put("triggers", triggersConfig);

        return config;
    }

    /**
     * Deserializes a {@code CheckpointCourse} from a {@code ConfigurationSection}.
     *
     * @param config the {@code ConfigurationSection} to deserialize from
     * @return the deserialized {@code CheckpointCourse}
     */
    static CheckpointCourse deserialize(ConfigurationSection config) throws CheckpointException {
        String courseName = config.getName().toLowerCase();
        CheckpointCourse course = new CheckpointCourse(courseName);

        List<?> checkpointsConfig = config.getList("checkpoints");
        course.loadCheckpoints(checkpointsConfig);

        List<?> triggersConfig = config.getList("triggers");
        course.loadTriggers(triggersConfig);

        return course;
    }

    private void loadCheckpoints(List<?> sections) throws CheckpointException {
        checkpoints = new ArrayList<>();
        checkpointMap = new HashMap<>();
        for (Object section : sections) {
            if (!(section instanceof Map)) {
                throw new CheckpointException("Malformed checkpoint");
            }

            Checkpoint checkpoint;
            try {
                checkpoint = Checkpoint.deserialize(this, (Map<String, Object>) section);
            } catch (CheckpointException e) {
                e.prepend("Unable to load checkpoint").printStackTrace();
                continue;
            }
            checkpoints.add(checkpoint);
            checkpointMap.put(checkpoint.getLabel(), checkpoint);
        }
    }

    private void loadTriggers(List<?> sections) throws CheckpointException {
        triggers = new ArrayList<>();
        for (Object section : sections) {
            Trigger trigger;
            try {
                trigger = Trigger.deserialize(this, (Map<String, Object>) section);
            } catch (CheckpointException e) {
                throw e.prepend("Unable to load trigger");
            }
            triggers.add(trigger);
        }
        plugin.reindexTriggers();
    }

}
