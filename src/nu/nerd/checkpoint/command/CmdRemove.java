package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointException;
import nu.nerd.checkpoint.CheckpointPlugin;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdRemove extends CheckpointCommand {

    public CmdRemove(CheckpointPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws CheckpointException {
        if (args.size() != 2) {
            throw new CheckpointException(null, true);
        }

        String name = args.get(0).toLowerCase();
        CheckpointCourse course = plugin.getCourse(name);

        String label = args.get(1).toLowerCase();

        Checkpoint checkpoint = course.removeCheckpoint(label);
        if (checkpoint == null) {
            return "No checkpoint with label {{" + label + "}} exists.";
        }

        return "Checkpoint {{" + label + "}} removed from course {{" + name + "}}.";
    }

    @Override
    public String getName(){
        return "remove";
    }

    @Override
    public String getDescription() {
        return "removes the checkpoint with the given label";
    }

    @Override
    public String getUsage(Player player) {
        return "<course> <label>";
    }

}
