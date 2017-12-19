package nu.nerd.checkpoint.command.checkpoint;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Queue;

@DescribableMeta(
        name = "order",
        description = "reorders a checkpoint to the specified index",
        usage = "<label> <index>"
)
public class CmdCheckpointModifyOrder extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() != 2) {
            throw new UsageException(this);
        }

        CheckpointCourse course = player.getCourse();
        String label = args.poll().toLowerCase();
        Checkpoint checkpoint = course.getCheckpoint(label);

        int index;
        try {
            index = Integer.parseInt(args.poll());
        } catch (NumberFormatException e) {
            throw new UsageException(this, "index must be an integer");
        }

        course.reorderCheckpoint(checkpoint, index);

        return "Checkpoint {{" + label + "}} reordered to index {{" + index + "}}.";
    }

}
