package nu.nerd.checkpoint.command.checkpoint;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.action.Action;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.command.DirectoryCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import nu.nerd.checkpoint.trigger.Trigger;

import java.util.Queue;

@DescribableMeta(
        name = "modify",
        description = "modify a trigger",
        usage = "<index> <property> <value>"
)
public class CmdCheckpointModify extends CheckpointCommand {


    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() < 1) {
            throw new UsageException(this);
        }

        CheckpointCourse course = player.getCourse();
        int index;
        try {
            index = Integer.parseInt(args.poll());
        } catch (NumberFormatException e) {
            throw new UsageException(this, "index must be an integer");
        }
        Trigger trigger = course.getTrigger(index);
        Action action = trigger.getAction();

        action.
    }
}
