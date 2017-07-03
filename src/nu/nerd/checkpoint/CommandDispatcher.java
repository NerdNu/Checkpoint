package nu.nerd.checkpoint;

import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.command.CmdAdd;
import nu.nerd.checkpoint.command.CmdCreateCourse;
import nu.nerd.checkpoint.command.CmdInfo;
import nu.nerd.checkpoint.command.CmdReload;
import nu.nerd.checkpoint.command.CmdRemove;
import nu.nerd.checkpoint.command.CmdRemoveCourse;
import nu.nerd.checkpoint.command.CmdSetGiver;
import nu.nerd.checkpoint.command.CmdSetItem;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dispatches commands to be executed by their respective {@code CheckpointCommand}s.
 */
public class CommandDispatcher {

    private CheckpointPlugin plugin;
    private CheckpointCommand[] commands;
    private Map<String, CheckpointCommand> commandMap;

    /**
     * Creates a new {@code CommandDispatcher} for the plugin instance.
     *
     * @param plugin the plugin instance
     */
    public CommandDispatcher(CheckpointPlugin plugin) {
        this.plugin = plugin;
        commands = new CheckpointCommand[]{
                new CmdCreateCourse(plugin),
                new CmdRemoveCourse(plugin),
                new CmdSetItem(plugin),
                new CmdSetGiver(plugin),
                new CmdInfo(plugin),
                new CmdAdd(plugin),
                new CmdRemove(plugin),
                new CmdReload(plugin)
        };

        commandMap = new HashMap<>();
        for (CheckpointCommand command : commands) {
            commandMap.put(command.getName(), command);
            for (String alias : command.getAliases()) {
                commandMap.put(alias, command);
            }
        }
    }

    /**
     * Executes the {@code checkpoint} command for the given player with the given list of arguments.
     *
     * @param player the player executing the command
     * @param args the arguments to the command
     */
    public void execute(Player player, List<String> args) {
        if (args.isEmpty()) {
            printHelp(player);
        } else {
            CheckpointCommand command = commandMap.get(args.get(0).toLowerCase());
            if (command == null) {
                printHelp(player);
            } else {
                try {
                    String msg = command.execute(player, args.subList(1, args.size()));
                    if (!msg.isEmpty()) {
                        plugin.message(player, msg);
                    }
                } catch (CheckpointException e) {
                    if (e.hasMessage()) {
                        plugin.message(player, e.getMessage());
                    }
                    if (e.isShowUsage()) {
                        plugin.message(player, "Usage: " + usageString(command, player));
                    }
                }
            }
        }
    }

    /**
     * Sends generic SafeHorses help text to the given player.
     *
     * @param player the player to send help to
     */
    private void printHelp(Player player) {
        plugin.message(player, "Checkpoint Commands");
        for (CheckpointCommand command : commands) {
            String usageArgs = command.getUsage(player);
            if (usageArgs == null) {
                continue;
            }

            StringBuilder str = new StringBuilder("{{/checkpoint ");
            str.append(command.getName());

            if (!usageArgs.isEmpty()) {
                str.append(" ").append(usageArgs);
            }
            str.append("}} - ").append(command.getDescription());
            plugin.message(player, str.toString());
        }
    }

    /**
     * Returns the usage string for a command based on the given player's
     * permissions.
     *
     * @param command the command
     * @param player the player to check permissions for
     * @return the usage string
     */
    private String usageString(CheckpointCommand command, Player player) {
        StringBuilder usage = new StringBuilder("{{/checkpoint ");
        usage.append(command.getName());

        String usageArgs = command.getUsage(player);
        if (!usageArgs.isEmpty()) {
            usage.append(" ").append(usageArgs);
        }
        usage.append("}}");
        return usage.toString();
    }


}
