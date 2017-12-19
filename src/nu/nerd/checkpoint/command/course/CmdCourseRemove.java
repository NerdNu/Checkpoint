package nu.nerd.checkpoint.command.course;

import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Queue;

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

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "removes an existing course";
    }

    @Override
    public String getUsage() {
        return "<course>";
    }

}
