package nu.nerd.checkpoint;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@code Player} wrapper for Checkpoint.
 */
public class CheckpointPlayer {

    private CheckpointPlugin plugin;
    private Player player;
    private Map<String, Checkpoint> checkpoints;

    /**
     * Creates a {@code CheckpointPlayer} for the given player.
     *
     * @param plugin the plugin instance
     * @param player the player
     */
    public CheckpointPlayer(CheckpointPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        loadConfig();
    }

    /**
     * Sets a {@code Checkpoint} for a player.
     *
     * @param checkpoint the {@code Checkpoint}
     */
    public void setCheckpoint(Checkpoint checkpoint) {
        checkpoints.put(checkpoint.getCourse().getName(), checkpoint);
        saveConfig();
    }

    /**
     * Gets the last {@code Checkpoint} visited for the given {@code CheckpointCourse}.
     *
     * @param course the {@code CheckpointCourse}
     * @return the {@code Checkpoint}
     */
    public Checkpoint getCheckpoint(CheckpointCourse course) {
        return checkpoints.get(course.getName());
    }

    /**
     * Loads the player's checkpoints from configuration.
     */
    public void loadConfig() {
        checkpoints = new HashMap<>();
        File configFile = getConfigFile();
        if (configFile.exists()) {
            ConfigurationSection config = YamlConfiguration.loadConfiguration(configFile);
            ConfigurationSection checkpointsConfig = config.getConfigurationSection("checkpoints");
            for (String name : checkpointsConfig.getKeys(false)) {
                try {
                    CheckpointCourse course = plugin.getCourse(name.toLowerCase());
                    String label = checkpointsConfig.getString(name);
                    Checkpoint checkpoint = course.getCheckpoint(label);
                    if (checkpoint == null) {
                        plugin.getLogger().warning("Unknown checkpoint '" + label + "' in " + configFile.getPath());
                    } else {
                        checkpoints.put(name, checkpoint);
                    }
                } catch (CheckpointException e) {
                    plugin.getLogger().warning("Unknown course '" + name + "' in " + configFile.getPath());
                }
            }
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
            for (Map.Entry<String, Checkpoint> entry : checkpoints.entrySet()) {
                checkpointsConfig.put(entry.getKey(), entry.getValue().getLabel());
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
    public File getConfigFile() {
        return new File(plugin.getDataFolder(), "players" + File.separator + player.getUniqueId().toString() + ".yml");
    }


}
