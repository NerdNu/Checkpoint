package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Queue;

public class CmdCheckpointRemove extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new UsageException(this);
        }

        CheckpointCourse course = player.getCourse();
        String label = args.poll().toLowerCase();

        course.removeCheckpoint(label);

        return "Checkpoint {{" + label + "}} removed from course {{" + course.getName() + "}}.";
    }

    @Override
    public String getName(){
        return "remove";
    }

    @Override
    public String getDescription() {
        return "removes the checkpoint with the given label";
    }

    @Override
    public String getUsage() {
        return "<label>";
    }

}
