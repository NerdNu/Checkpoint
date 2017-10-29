package nu.nerd.checkpoint;

import nu.nerd.checkpoint.exception.CheckpointException;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * A checkpoint in a {@code CheckpointCourse}.
 */
public class Checkpoint {

    private CheckpointCourse course;
    private String label;
    private Location location;
    private ItemStack icon;

    /**
     * Creates a {@code Checkpoint}.
     *
     * @param course the checkpoint course this belongs to
     * @param label a label for the checkpoint
     * @param location the location to teleport the player to
     */
    public Checkpoint(CheckpointCourse course, String label, Location location) {
        this(course, label, location, null);
    }

    /**
     * Creates a {@code Checkpoint}.
     *
     * @param course the checkpoint course this belongs to
     * @param label a label for the checkpoint
     * @param location the location to teleport the player to
     * @param icon an ItemStack that represents the checkpoint in the index
     */
    public Checkpoint(CheckpointCourse course, String label, Location location, ItemStack icon) {
        this.course = course;
        this.label = label;
        this.location = location;
        this.icon = icon;
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
     * Returns the location that the checkpoint teleports players to.
     *
     * @return the target location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location to the given location.
     *
     * @param location the new location
     */
    public void setLocation(Location location) {
        this.location = location;

        CheckpointPlugin plugin = CheckpointPlugin.getInstance();
        plugin.saveToConfig();
    }

    /**
     * Returns the icon for this checkpoint, or {@code null} if none exists.
     * @return the icon
     */
    public ItemStack getIcon() {
        return icon;
    }

    /**
     * Sets the icon to the given ItemStack.
     *
     * @param icon the new icon
     */
    public void setIcon(ItemStack icon) {
        this.icon = icon.clone();

        CheckpointPlugin plugin = CheckpointPlugin.getInstance();
        plugin.saveToConfig();
    }

    /**
     * Serializes the {@code Checkpoint} to a {@code Map}.
     *
     * @return the serialized {@code Map}
     */
    Map<String, Object> serialize() {
        Map<String, Object> config = new HashMap<>();

        config.put("label", label);
        config.put("location", location.serialize());
        config.put("icon", icon == null ? null : icon.serialize());
        return config;
    }

    /**
     * Deserializes a {@code Checkpoint} from a {@code ConfigurationSection}.
     *
     * @param course the {@code CheckpointCourse}
     * @param config the config to deserialize from
     * @return the deserialized {@code Checkpoint}
     * @throws CheckpointException if the config could not be parsed into a checkpoint
     */
    static Checkpoint deserialize(CheckpointCourse course, Map<String, Object> config) throws CheckpointException {
        String label = Utils.getString(config, "label");
        if (label == null) {
            throw new CheckpointException("No label provided");
        }

        Map<String, Object> locationConfig = Utils.getSection(config, "location");
        if (locationConfig == null) {
            throw new CheckpointException("No location provided");
        }
        Location location = Location.deserialize(locationConfig);

        Map<String, Object> iconConfig = Utils.getSection(config,"icon");
        ItemStack icon = null;
        if (iconConfig != null) {
            icon = ItemStack.deserialize(iconConfig);
        }

        return new Checkpoint(course, label, location, icon);
    }

}
