package nu.nerd.checkpoint.command.course;

import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Queue;

@DescribableMeta(
        name = "remove",
        description = "removes an existing course",
        usage = "<course>"
)
public class CmdCourseRemove extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new UsageException(this);
        }

        String name = args.poll().toLowerCase();
        plugin.removeCourse(name);
        return "Course {{" + name + "}} removed.";
    }

}
