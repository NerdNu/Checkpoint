package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Queue;

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

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "creates a new course";
    }

    @Override
    public String getUsage() {
        return "<course>";
    }

}
