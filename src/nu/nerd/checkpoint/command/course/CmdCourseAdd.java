package nu.nerd.checkpoint.command.course;

import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Queue;

@DescribableMeta(
        name = "add",
        description = "creates a new course",
        usage = "<course>"
)
public class CmdCourseAdd extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new UsageException(this);
        }

        String name = args.poll().toLowerCase();
        plugin.addCourse(name);
        return "Course {{" + name + "}} created.";
    }

}
