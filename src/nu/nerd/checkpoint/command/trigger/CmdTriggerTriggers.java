package nu.nerd.checkpoint.command.trigger;

import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;

import java.util.Queue;

public class CmdTriggerTriggers extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        return "Available trigger types:"
            + "\n{{block}} - triggered when a player interacts with the block you're looking at"
            + "\n{{item}} - triggered when a player clicks using the item you're holding";
    }

    @Override
    public String getName() {
        return "triggers";
    }

    @Override
    public String getDescription() {
        return "lists available trigger types";
    }

    @Override
    public String getUsage() {
        return "";
    }

}
