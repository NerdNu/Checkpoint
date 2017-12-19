package nu.nerd.checkpoint.command.checkpoint;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import org.bukkit.Location;

import java.util.Queue;

@DescribableMeta(
        name = "location",
        description = "sets the location to your current location",
        usage = "<label>"
)
public class CmdCheckpointModifyLocation extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new UsageException(this);
        }

        CheckpointCourse course = player.getCourse();
        String label = args.poll().toLowerCase();
        Checkpoint checkpoint = course.getCheckpoint(label);
        Location location = player.getPlayer().getLocation();

        checkpoint.setLocation(location);

        return "Checkpoint {{" + label + "}} location set to " + Utils.formatLocation(location) + ".";
    }

}
