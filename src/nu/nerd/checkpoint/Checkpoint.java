package nu.nerd.checkpoint;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

/**
 * A checkpoint in a {@code CheckpointCourse}.
 */
public class Checkpoint {

    private CheckpointCourse course;
    private String label;
    private Location block;
    private Location target;

    /**
     * Creates a {@code Checkpoint}.
     *
     * @param course the checkpoint course this belongs to
     * @param label a label for the checkpoint
     * @param block the location of the block that sets the checkpoint
     * @param target the location to teleport the player to
     */
    public Checkpoint(CheckpointCourse course, String label, Location block, Location target) {
        this.course = course;
        this.label = label;
        this.block = block;
        this.target = target;
    }

    /**
     * Returns the {@code CheckpointCourse} that this belongs to.
     *
     * @return the {@code CheckpointCourse}
     */
    public CheckpointCourse getCourse() {
        return course;
    }

    /**
     * Returns the label of the checkpoint.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the location of the block that sets the checkpoint.
     *
     * @return the block location
     */
    public Location getBlock() {
        return block;
    }

    /**
     * Returns the location that the checkpoint teleports players to.
     *
     * @return the target location
     */
    public Location getTarget() {
        return target;
    }

    /**
     * Serializes the {@code Checkpoint} to a {@code Map}.
     *
     * @return the serialized {@code Map}
     */
    public Map<String, Object> serialize() {
        Map<String, Object> config = new HashMap<>();

        config.put("block", block.serialize());
        config.put("target", target.serialize());
        return config;
    }

    /**
     * Deserializes a {@code Checkpoint} from a {@code ConfigurationSection}.
     *
     * @param course the {@code CheckpointCourse}
     * @param label the label
     * @param config the {@code ConfigurationSection} to deserialize from
     * @return the deserialized {@code Checkpoint}
     */
    public static Checkpoint deserialize(CheckpointCourse course, String label, ConfigurationSection config) {
        ConfigurationSection blockConfig = config.getConfigurationSection("block");
        Location block = Location.deserialize(blockConfig.getValues(true));

        ConfigurationSection targetConfig = config.getConfigurationSection("target");
        Location target = Location.deserialize(targetConfig.getValues(true));

        return new Checkpoint(course, label, block, target);
    }

}
