package nu.nerd.checkpoint;

import nu.nerd.checkpoint.exception.CheckpointException;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A {@code Player} wrapper for Checkpoint.
 */
public class CheckpointPlayer {

    private final CheckpointPlugin plugin;
    private final Player player;
    private CheckpointCourse course;
    private Map<String, LinkedHashSet<Checkpoint>> checkpoints;
    private IndexView indexView;

    private static final Pattern messagePattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
    private static final ChatColor primaryColor = ChatColor.AQUA;
    private static final ChatColor secondaryColor = ChatColor.LIGHT_PURPLE;
    private static final String highlightString = secondaryColor + "$1" + primaryColor;

    /**
     * Creates a {@code CheckpointPlayer} for the given player.
     *
     * @param player the player
     */
    public CheckpointPlayer(Player player) {
        plugin = CheckpointPlugin.getInstance();
        this.player = player;
        this.course = null;
        this.checkpoints = new HashMap<>();
        loadConfig();
    }

    /**
     * Sets a {@code Checkpoint} for a player.
     *
     * @param checkpoint the {@code Checkpoint}
     */
    public void setCheckpoint(Checkpoint checkpoint) {
        CheckpointCourse course = checkpoint.getCourse();
        LinkedHashSet<Checkpoint> courseCheckpoints;
        if (checkpoints.containsKey(course.getName())) {
            courseCheckpoints = checkpoints.get(course.getName());
        } else {
            courseCheckpoints = new LinkedHashSet<>();
            checkpoints.put(course.getName(), courseCheckpoints);
        }
        courseCheckpoints.add(checkpoint);
        saveConfig();
    }

    /**
     * Gets the last {@code Checkpoint} visited for the given {@code CheckpointCourse}.
     *
     * @param course the {@code CheckpointCourse}
     * @return the {@code Checkpoint}
     */
    public Checkpoint getLastCheckpoint(CheckpointCourse course) {
        LinkedHashSet<Checkpoint> checkpointSet = checkpoints.get(course.getName());

        Checkpoint lastCheckpoint = null;
        for (Checkpoint checkpoint : checkpointSet) {
            lastCheckpoint = checkpoint;
        }
        return lastCheckpoint;
    }

    /**
     * Sets the player's selected course to the given {@code CheckpointCourse}.
     *
     * @param course the course
     */
    public void setCourse(CheckpointCourse course) {
        this.course = course;
    }

    /**
     * Gets the current {@code CheckpointCourse} for the player.
     *
     * @return the current course
     * @throws CheckpointException if the player has no course selected
     */
    public CheckpointCourse getCourse() throws CheckpointException {
        if (course == null) {
            throw new CheckpointException("You have no course selected. Run {{/course <course>}} to select one.");
        }
        return course;
    }

    /**
     * Gets the Bukkit {@code Player}.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gives an {@code ItemStack} to the player.
     *
     * @param item the item to give
     */
    public void giveItem(ItemStack item) {
        player.getInventory().addItem(item);
    }

    /**
     * Returns the item the player is currently holding.
     *
     * @return the item
     */
    public ItemStack getHeldItem() {
        return player.getInventory().getItemInMainHand();
    }

    /**
     * Teleports the player to the location of the given {@code Checkpoint}.
     *
     * @param checkpoint the checkpoint
     */
    public void teleport(Checkpoint checkpoint) {
        player.teleport(checkpoint.getLocation());
    }

    /**
     * Returns true if the given inventory is the player's open inventory UI.
     * @param inventory the inventory to check
     * @return whether the inventory is an index view UI
     */
    public boolean hasIndex(Inventory inventory) {
        if (indexView == null) {
            return false;
        } else {
            return indexView.hasInventory(inventory);
        }
    }

    /**
     * Opens an inventory UI for the player to select a checkpoint to travel to.
     * @param course the course to open the index for
     */
    public void openIndex(CheckpointCourse course) {
        closeIndex();
        indexView = IndexView.open(this, course);
    }

    /**
     * Closes the index view if it is open.
     */
    public void closeIndex() {
        if (indexView != null) {
            indexView.close();
            indexView = null;
        }
    }

    /**
     * Sends a colorful message to the player.
     *
     * @param message the message
     */
    public void message(String message) {
        String coloredMessage = primaryColor + messagePattern.matcher(message).replaceAll(highlightString);
        player.sendMessage(coloredMessage);
    }

    /**
     * Loads the player's checkpoints from configuration.
     */
    public void loadConfig() {
        course = null;
        File configFile = getConfigFile();
        if (configFile.exists()) {
            ConfigurationSection config = YamlConfiguration.loadConfiguration(configFile);
            ConfigurationSection checkpointsConfig = config.getConfigurationSection("checkpoints");
            loadCheckpoints(checkpointsConfig);
        }
        closeIndex();
    }

    private void loadCheckpoints(ConfigurationSection checkpointsConfig) {
        checkpoints = new HashMap<>();
        for (String name : checkpointsConfig.getKeys(false)) {
            CheckpointCourse course;
            try {
                course = plugin.getCourse(name.toLowerCase());
            } catch (CheckpointException e) {
                e.prepend("Error loading checkpoints for player {{" + player.getName() + "}} in course {{"
                        + name + "}}");
                plugin.getLogger().warning(e.getMessage());
                continue;
            }

            List<String> checkpointLabels = checkpointsConfig.getStringList(name);
            LinkedHashSet<Checkpoint> checkpointSet = new LinkedHashSet<>();
            for (String label : checkpointLabels) {
                Checkpoint checkpoint;
                try {
                    checkpoint = course.getCheckpoint(label);
                } catch (CheckpointException e) {
                    e.prepend("Error loading checkpoint for player {{" + player.getName() + "}} in course {{"
                            + name + "}}");
                    plugin.getLogger().warning(e.getMessage());
                    continue;
                }

                checkpointSet.add(checkpoint);
            }
            checkpoints.put(name, checkpointSet);
        }
    }

    /**
     * Saves the player's checkpoints to configuration.
     */
    public void saveConfig() {
        File configFile = getConfigFile();
        if (!checkpoints.isEmpty()) {
            YamlConfiguration config = new YamlConfiguration();

            Map<String, Object> checkpointsConfig = new HashMap<>();
            for (Map.Entry<String, LinkedHashSet<Checkpoint>> entry : checkpoints.entrySet()) {
                List<String> checkpointLabels = new ArrayList<>();
                for (Checkpoint checkpoint : entry.getValue()) {
                    checkpointLabels.add(checkpoint.getLabel());
                }
                checkpointsConfig.put(entry.getKey(), checkpointLabels);
            }
            config.set("checkpoints", checkpointsConfig);

            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the config file for this player.
     *
     * @return the config file
     */
    private File getConfigFile() {
        return new File(plugin.getDataFolder(), "players" + File.separator + player.getUniqueId().toString() + ".yml");
    }


}
