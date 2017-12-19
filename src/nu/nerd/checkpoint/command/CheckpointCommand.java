package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.Describable;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.CheckpointPlugin;

import java.util.Queue;

/**
 * A command for the Checkpoint plugin.
 */
public abstract class CheckpointCommand extends Describable {

    /**
     * The plugin instance
     */
    protected CheckpointPlugin plugin;

    private CheckpointCommand parent;

    public CheckpointCommand() {
        plugin = CheckpointPlugin.getInstance();
    }

    void setParent(CheckpointCommand parent) {
        this.parent = parent;
    }

    /**
     * Executes the command by the given player with a list of arguments.
     *
     * @param player the player running the command
     * @param args the arguments to the command
     * @return the message to send to the player upon completion of the command execution
     */
    public abstract String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException;

    /**
     * Returns a list of strings that are aliases of this command.
     *
     * @return the command's aliases
     */
    public String[] getAliases() {
        return new String[]{};
    }

    /**
     * Returns a string containing the command name and usage parameters.
     *
     * @return the usage string
     */
    public String getUsageString() {
        String commandString = getCommandString();
        String usage = getUsage();
        if (usage.isEmpty()) {
            return commandString;
        } else {
            return commandString + " " + usage;
        }
    }

    protected void printHelp(CheckpointPlayer player) {
        player.message(getHelp());
    }

    /**
     * Returns the help string for the command.
     *
     * @return the help string
     */
    public String getHelp() {
        String usageString = getUsageString();
        String description = getDescription();

        return "{{" + usageString + "}} - " + description;
    }

    private String getCommandString() {
        String commandString = getName();
        if (parent == null) {
            commandString = "/" + commandString;
        } else {
            commandString = parent.getCommandString() + " " + commandString;
        }
        return commandString;
    }

}
