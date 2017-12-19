package nu.nerd.checkpoint.command.checkpoint;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Queue;

public class CmdCheckpointVisit extends CheckpointCommand {
    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() < 1 || args.size() > 2) {
            throw new UsageException(this);
        }

        CheckpointCourse course = player.getCourse();
        Checkpoint checkpoint = course.getCheckpoint(args.poll());

        boolean visited = true;
        if (!args.isEmpty()) {
            String visitedParam = args.poll();
            if (visitedParam.equalsIgnoreCase("false")) {
                visited = false;
            } else if (!visitedParam.equalsIgnoreCase("true")) {
                throw new UsageException(this, "visited must be true or false");
            }
        }

        if (visited) {
            player.setCheckpoint(checkpoint);
        } else {
            player.unsetCheckpoint(checkpoint);
        }
        return "Set visited for checkpoint {{" + checkpoint.getLabel() + "}} to {{" + visited + "}}.";
    }

    @Override
    public String getName() {
        return "visit";
    }

    @Override
    public String getDescription() {
        return "sets the checkpoint as visited for you, defaults to true";
    }

    @Override
    public String getUsage() {
        return "<label> [(true|false)]";
    }

}
