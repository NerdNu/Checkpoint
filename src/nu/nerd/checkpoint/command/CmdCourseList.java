package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Queue;

public class CmdCourseList extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (!args.isEmpty()) {
            throw new UsageException(this);
        }

        StringBuilder builder = new StringBuilder("Available courses:");
        for (CheckpointCourse course : plugin.getCourses()) {
            int checkpointCount = course.checkpointCount();
            int triggerCount = course.getTriggers().size();
            builder.append("\n{{").append(course.getName()).append("}} (").append(checkpointCount)
                    .append(" checkpoints, ").append(triggerCount).append(" triggers)");
        }
        return builder.toString();
    }

    @Override
    public String getName(){
        return "list";
    }

    @Override
    public String getDescription() {
        return "shows a list of available courses";
    }

}
