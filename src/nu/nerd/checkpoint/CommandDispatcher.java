package nu.nerd.checkpoint;

import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.command.CmdCheckpoint;
import nu.nerd.checkpoint.command.CmdCourse;
import nu.nerd.checkpoint.command.CmdTrigger;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Dispatches commands to be executed by their respective {@code CheckpointCommand}s.
 */
public class CommandDispatcher {

    private CheckpointPlugin plugin;
    private CheckpointCommand[] commands;
    private Map<String, CheckpointCommand> commandMap;

    /**
     * Creates a new {@code CommandDispatcher} for the plugin instance.
     */
    public CommandDispatcher() {
        this.plugin = CheckpointPlugin.getInstance();
        commands = new CheckpointCommand[]{
                new CmdCheckpoint(),
                new CmdCourse(),
                new CmdTrigger(),
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
    public void execute(CheckpointPlayer player, Queue<String> args) {
        if (args.isEmpty()) {
            printHelp(player);
        } else {
            CheckpointCommand command = commandMap.get(args.poll().toLowerCase());
            if (command == null) {
                printHelp(player);
            } else {
                try {
                    String msg = command.execute(player, args);
                    if (!msg.isEmpty()) {
                        player.message(msg);
                    }
                } catch (UsageException e) {
                    if (e.hasMessage()) {
                        player.message(e.getMessage());
                    }
                    if (e.command != null) {
                        player.message("Usage: {{" + e.command.getUsageString() + "}}\n" + e.command.getDescription());
                    } else {
                        player.message("There was an error executing your command.");
                    }
                } catch (CheckpointException e) {
                    if (e.hasMessage()) {
                        player.message(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Sends generic help text to the given player.
     *
     * @param player the player to send help to
     */
    private void printHelp(CheckpointPlayer player) {
        StringBuilder builder = new StringBuilder();
        builder.append("Checkpoint Commands");
        for (CheckpointCommand command : commands) {
            builder.append("\n").append(command.getHelp());
        }
        player.message(builder.toString());
    }

}
