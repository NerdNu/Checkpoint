package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import org.bukkit.Location;

import java.util.Queue;

public class CmdTriggerActions extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        return "Available action types:"
            + "\n{{checkpoint <label>}} - teleports the player to a checkpoint"
            + "\n{{give-item}} - gives the player a copy of the item you're holding"
            + "\n{{set-checkpoint <label>}} - marks the player as having visited a checkpoint"
            + "\n{{open-index}} - allows the player to select from visited checkpoints"
            + "\n{{sleep}} - sets the player's bed location";
    }

    @Override
    public String getName() {
        return "actions";
    }

    @Override
    public String getDescription() {
        return "lists available action types";
    }

    @Override
    public String getUsage() {
        return "";
    }

}
