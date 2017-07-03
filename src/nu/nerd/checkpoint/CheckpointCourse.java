package nu.nerd.checkpoint;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A course containing a set of {@code Checkpoint}s.
 */
public class CheckpointCourse implements Iterable<Checkpoint> {

    private CheckpointPlugin plugin;
    private ItemStack item;
    private Location itemGiver;
    private Map<String, Checkpoint> checkpoints;
    private String name;

    /**
     * Creates a new {@code CheckpointCourse}.
     *
     * @param plugin the plugin instance
     * @param name the course's name
     */
    public CheckpointCourse(CheckpointPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        checkpoints = new HashMap<>();
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
     * Returns the {@code ItemStack} that teleports players to their last checkpoint.
     *
     * @return the course's teleporter {@code ItemStack}
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Sets the {@code ItemStack} that teleports players to their last checkpoint.
     *
     * @param item the teleporter {@code ItemStack} to set
     */
    public void setItem(ItemStack item) {
        if (this.item != null) {
            plugin.itemHash.remove(this.item);
        }
        this.item = item;
        plugin.itemHash.put(item, this);
        plugin.saveToConfig();
    }

    /**
     * Returns the {@code Location} of the block that gives players the teleporter item.
     * @return the {@code Location} of the item giver
     */
    public Location getItemGiver() {
        return itemGiver;
    }

    /**
     * Sets the {@code Location} of the block that gives players the teleporter item.
     * @param location the item giver {@code Location} to set
     */
    public void setItemGiver(Location location) {
        if (this.itemGiver != null) {
            plugin.giverHash.remove(this.itemGiver);
        }
        this.itemGiver = location;
        plugin.giverHash.put(location, this);
        plugin.saveToConfig();
    }

    /**
     * Adds a {@code Checkpoint} to the course.
     *
     * @param label a label for the {@code Checkpoint}
     * @param block the location of the block that sets the checkpoint
     * @param target the location that the {@code Checkpoint} teleports the player to
     * @return the {@code Checkoint} if it was added, or {@code null} if a checkpoint with the same label exists
     */
    public Checkpoint addCheckpoint(String label, Location block, Location target) {
        if (checkpoints.containsKey(label)) {
            return null;
        }

        Checkpoint checkpoint = new Checkpoint(this, label, block, target);
        checkpoints.put(label, checkpoint);
        plugin.checkpointHash.put(block, checkpoint);
        plugin.saveToConfig();
        return checkpoint;
    }

    /**
     * Removes the {@code Checkpoint} with the given label from the course.
     *
     * @param label the label of the {@code Checkpoint} to remove
     * @return the {@code Checkpoint} if it was removed, or {@code null} otherwise
     */
    public Checkpoint removeCheckpoint(String label) {
        Checkpoint checkpoint = checkpoints.remove(label);
        plugin.checkpointHash.remove(checkpoint.getBlock());
        plugin.saveToConfig();
        return checkpoint;
    }

    /**
     * Removes the given {@code Checkpoint} from the course.
     *
     * @param checkpoint the {@code Checkpoint} to remove
     * @return the {@code Checkpoint} if it was removed, or {@code null} otherwise
     */
    public Checkpoint removeCheckpoint(Checkpoint checkpoint) {
        return removeCheckpoint(checkpoint.getLabel());
    }

    /**
     * Returns the {@code Checkpoint} with the given label, or {@code null} if none exists.
     *
     * @param label the label of the {@code Checkpoint} to search for
     * @return the {@code Checkpoint} with the given label, or {@code null} if none exists
     */
    public Checkpoint getCheckpoint(String label) {
        return checkpoints.get(label);
    }

    @Override
    public Iterator<Checkpoint> iterator() {
        return checkpoints.values().iterator();
    }

    /**
     * Serializes the {@code CheckpointCourse} to a {@code Map}.
     *
     * @return the serialized {@code Map}
     */
    public Map<String, Object> serialize() {
        Map<String, Object> config = new HashMap<>();

        if (item != null) {
            config.put("item", item.serialize());
        }

        if (itemGiver != null) {
            config.put("giver", itemGiver.serialize());
        }

        Map<String, Object> checkpointsConfig = new HashMap<>();
        for (Map.Entry<String, Checkpoint> entry : checkpoints.entrySet()) {
            checkpointsConfig.put(entry.getKey(), entry.getValue().serialize());
        }
        config.put("checkpoints", checkpointsConfig);

        return config;
    }

    /**
     * Deserializes a {@code CheckpointCourse} from a {@code ConfigurationSection}.
     *
     * @param plugin the plugin instance
     * @param config the {@code ConfigurationSection} to deserialize from
     * @return the deserialized {@code CheckpointCourse}
     */
    public static CheckpointCourse deserialize(CheckpointPlugin plugin, ConfigurationSection config) {
        CheckpointCourse course = new CheckpointCourse(plugin, config.getName().toLowerCase());

        ConfigurationSection itemConfig = config.getConfigurationSection("item");
        if (itemConfig != null) {
            ItemStack item = ItemStack.deserialize(itemConfig.getValues(true));
            course.item = item;
            plugin.itemHash.put(item, course);
        }

        ConfigurationSection giverConfig = config.getConfigurationSection("giver");
        if (giverConfig != null) {
            Location giver = Location.deserialize(giverConfig.getValues(true));
            course.itemGiver = giver;
            plugin.giverHash.put(giver, course);
        }

        ConfigurationSection checkpointsConfig = config.getConfigurationSection("checkpoints");
        for (String label : checkpointsConfig.getKeys(false)) {
            Checkpoint checkpoint = Checkpoint.deserialize(course, label.toLowerCase(),
                    checkpointsConfig.getConfigurationSection(label));
            course.checkpoints.put(label.toLowerCase(), checkpoint);
            plugin.checkpointHash.put(checkpoint.getBlock(), checkpoint);
        }

        return course;
    }

}
