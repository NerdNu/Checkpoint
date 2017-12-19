package nu.nerd.checkpoint.command.checkpoint;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import org.bukkit.Location;

import java.util.Queue;

public class CmdCheckpointAdd extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new UsageException(this);
        }

        CheckpointCourse course = player.getCourse();
        String label = args.poll().toLowerCase();
        Location location = player.getPlayer().getLocation();

        course.addCheckpoint(label, location);

        return "Checkpoint {{" + label + "}} created for course {{" + course.getName() + "}}.";
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "adds a checkpoint at your current location";
    }

    @Override
    public String getUsage() {
        return "<label>";
    }

}
