package nu.nerd.checkpoint;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * The Checkpoint plugin.
 */
public class CheckpointPlugin extends JavaPlugin implements Listener {

    protected Map<Location, Checkpoint> checkpointHash;
    protected Map<ItemStack, CheckpointCourse> itemHash;
    protected Map<Location, CheckpointCourse> giverHash;

    private Map<String, CheckpointCourse> courses;
    private Map<UUID, CheckpointPlayer> players;
    private CommandDispatcher dispatcher;

    private static final Pattern messagePattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
    private static final ChatColor primaryColor = ChatColor.AQUA;
    private static final ChatColor secondaryColor = ChatColor.LIGHT_PURPLE;
    private static final String highlightString = secondaryColor + "$1" + primaryColor;

    @Override
    public void onEnable() {
        dispatcher = new CommandDispatcher(this);
        players = new HashMap<>();
        saveDefaultConfig();
        loadFromConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (command.getName().equalsIgnoreCase("checkpoint")) {
            if (sender instanceof Player) {
                dispatcher.execute((Player) sender, Arrays.asList(args));
            } else {
                message(sender, "You must be a player to run this command.");
            }
            return true;
        }

        return false;
    }


    /**
     * Sends a colorful message to a player.
     *
     * @param sender the {@code CommandSender} to send the message to
     * @param message the message
     */
    public void message(CommandSender sender, String message) {
        sender.sendMessage(primaryColor + messagePattern.matcher(message).replaceAll(highlightString));
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
        checkpointHash = new HashMap<>();
        itemHash = new HashMap<>();
        giverHash = new HashMap<>();
        ConfigurationSection coursesConfig = getConfig().getConfigurationSection("courses");
        for (String name : coursesConfig.getKeys(false)) {
            courses.put(name, CheckpointCourse.deserialize(this, coursesConfig.getConfigurationSection(name)));
        }
        for (CheckpointPlayer player : players.values()) {
            player.loadConfig();
        }
    }

    /**
     * Returns the {@code CheckpointCourse} with the given name.
     *
     * @param name the name
     * @return the {@code CheckpointCourse} with the given name
     */
    public CheckpointCourse getCourse(String name) {
        if (!courses.containsKey(name)) {
            throw new CheckpointException("There is no course named {{" + name + "}}.", false);
        }

        return courses.get(name);
    }

    /**
     * Creates a new {@code CheckpointCourse} with the given name.
     *
     * @param name the name of the {@code CheckpointCourse} to create
     * @return the {@code CheckpointCourse} that was created
     */
    public CheckpointCourse addCourse(String name) {
        if (courses.containsKey(name)) {
            throw new CheckpointException("There is already a course named {{" + name + "}}.", false);
        }

        CheckpointCourse course = new CheckpointCourse(this, name);
        courses.put(name, course);
        saveToConfig();
        return course;
    }

    /**
     * Removes the {@code CheckpointCourse} with the given name.
     *
     * @param name the name of the {@code CheckpointCourse} to remove
     * @return the {@code CheckpointCourse} that was removed
     */
    public CheckpointCourse removeCourse(String name) {
        if (!courses.containsKey(name)) {
            throw new CheckpointException("There is no course named {{" + name + "}}.", false);
        }

        CheckpointCourse course = courses.remove(name);
        saveToConfig();
        return course;
    }

    /**
     * Returns the {@code CheckpointPlayer} for the given {@code Player}.
     *
     * @param player the {@code Player}
     * @return the {@code CheckpointPlayer}
     */
    public CheckpointPlayer getCheckpointPlayer(Player player) {
        return players.get(player.getUniqueId());
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        players.put(event.getPlayer().getUniqueId(), new CheckpointPlayer(this, event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        players.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (block != null && checkpointHash.containsKey(block.getLocation())) {
            Checkpoint checkpoint = checkpointHash.get(block.getLocation());
            getCheckpointPlayer(event.getPlayer()).setCheckpoint(checkpoint);
            message(event.getPlayer(), "Checkpoint set.");
            event.setCancelled(true);
            return;
        }

        if (block != null && giverHash.containsKey(block.getLocation())) {
            CheckpointCourse course = giverHash.get(block.getLocation());
            if (course.getItem() != null) {
                event.setCancelled(true);
                for (ItemStack stack : event.getPlayer().getInventory()) {
                    if (stack != null && stack.getType() == course.getItem().getType()) {
                        ItemStack clone = stack.clone();
                        clone.setAmount(1);
                        if (clone.equals(course.getItem())) {
                            message(event.getPlayer(), "You already have a teleporter item.");
                            return;
                        }
                    }
                }
                event.getPlayer().getInventory().addItem(course.getItem().clone());
                message(event.getPlayer(), "Here's a teleporter item! Click with it to return to your last checkpoint.");
            }
            return;
        }

        ItemStack item = event.getItem();
        if (item != null) {
            item = item.clone();
            item.setAmount(1);
            if (itemHash.containsKey(item)) {
                event.setCancelled(true);

                CheckpointCourse course = itemHash.get(item);
                CheckpointPlayer player = getCheckpointPlayer(event.getPlayer());
                Checkpoint checkpoint = player.getCheckpoint(course);
                if (checkpoint == null) {
                    message(event.getPlayer(), "You don't have any checkpoints set yet!");
                } else {
                    event.getPlayer().teleport(checkpoint.getTarget());
                }
            }
        }

    }

}
