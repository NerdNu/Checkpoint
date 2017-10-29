package nu.nerd.checkpoint;

import nu.nerd.checkpoint.exception.CheckpointException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The Checkpoint plugin.
 */
public class CheckpointPlugin extends JavaPlugin {

    private Map<String, CheckpointCourse> courses;
    private Map<UUID, CheckpointPlayer> players;
    private PlayerManager playerManager;
    private CommandDispatcher commandDispatcher;
    private TriggerDispatcher triggerDispatcher;
    private CheckpointListener listener;

    private static CheckpointPlugin instance;

    public static CheckpointPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        players = new HashMap<>();
        playerManager = new PlayerManager();
        commandDispatcher = new CommandDispatcher();
        triggerDispatcher = new TriggerDispatcher();
        listener = new CheckpointListener();
        getServer().getPluginManager().registerEvents(listener, this);

        loadFromConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CheckpointPlayer checkpointPlayer = getCheckpointPlayer(player);
            Deque<String> params = new ArrayDeque<>(Arrays.asList(args));
            params.addFirst(command.getName());

            commandDispatcher.execute(checkpointPlayer, params);
        } else {
            sender.sendMessage("You must be a player to run this command.");
        }
        return true;
    }

    /**
     * Saves configuration to {@code config.yml}.
     */
    public void saveToConfig() {
        Map<String, Object> coursesConfig = new HashMap<>();
        for (Map.Entry<String, CheckpointCourse> entry : courses.entrySet()) {
            coursesConfig.put(entry.getKey(), entry.getValue().serialize());
        }
        getConfig().set("courses", coursesConfig);
        saveConfig();
    }

    /**
     * Loads configuration from {@code config.yml} into memory.
     */
    public void loadFromConfig() {
        reloadConfig();
        courses = new HashMap<>();
        ConfigurationSection coursesConfig = getConfig().getConfigurationSection("courses");
        if (coursesConfig != null) {
            for (String name : coursesConfig.getKeys(false)) {
                try {
                    courses.put(name, CheckpointCourse.deserialize(coursesConfig.getConfigurationSection(name)));
                } catch (CheckpointException e) {
                    e.prepend("Error loading course '" + name + "'").printStackTrace();
                }
            }
        }
        for (CheckpointPlayer player : players.values()) {
            player.loadConfig();
        }
        reindexTriggers();
    }

    /**
     * Reindexes all triggers for all courses.
     */
    public void reindexTriggers() {
        triggerDispatcher.reindexTriggers();
    }

    /**
     * Returns the {@code CheckpointCourse} with the given name.
     *
     * @param name the name
     * @return the {@code CheckpointCourse} with the given name
     * @throws CheckpointException if no such course exists
     */
    public CheckpointCourse getCourse(String name) throws CheckpointException {
        if (!courses.containsKey(name)) {
            throw new CheckpointException("Unrecognized course {{" + name + "}}");
        }

        return courses.get(name);
    }

    /**
     * Creates a new {@code CheckpointCourse} with the given name.
     *
     * @param name the name of the {@code CheckpointCourse} to create
     * @return the {@code CheckpointCourse} that was created
     * @throws CheckpointException if a course with the given name already exists
     */
    public CheckpointCourse addCourse(String name) throws CheckpointException {
        if (courses.containsKey(name)) {
            throw new CheckpointException("There is already a course named {{" + name + "}}");
        }

        CheckpointCourse course = new CheckpointCourse(name);
        courses.put(name, course);
        saveToConfig();
        return course;
    }

    /**
     * Removes the {@code CheckpointCourse} with the given name.
     *
     * @param name the name of the {@code CheckpointCourse} to remove
     * @return the {@code CheckpointCourse} that was removed
     * @throws CheckpointException if no such course exists
     */
    public CheckpointCourse removeCourse(String name) throws CheckpointException {
        if (!courses.containsKey(name)) {
            throw new CheckpointException("There is no course named {{" + name + "}}.");
        }

        CheckpointCourse course = courses.remove(name);
        saveToConfig();
        return course;
    }

    /**
     * Returns a collection of registered courses.
     * @return the courses
     */
    public Collection<CheckpointCourse> getCourses() {
        return courses.values();
    }

    /**
     * Returns the {@code PlayerManager}.
     * @return the player manager
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    /**
     * Returns the {@code TriggerDispatcher}.
     * @return the trigger dispatcher
     */
    public TriggerDispatcher getTriggerDispatcher() {
        return triggerDispatcher;
    }

    /**
     * Returns the {@code CheckpointPlayer} for the given {@code Player}.
     *
     * @param player the {@code Player}
     * @return the {@code CheckpointPlayer}
     */
    public CheckpointPlayer getCheckpointPlayer(Player player) {
        return playerManager.wrap(player);
    }

}
