package nu.nerd.checkpoint.command.checkpoint;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Queue;

public class CmdCheckpointTp extends CheckpointCommand {
    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new UsageException(this);
        }

        CheckpointCourse course = player.getCourse();
        Checkpoint checkpoint = course.getCheckpoint(args.poll());

        player.teleport(checkpoint);
        return "";
    }

    @Override
    public String getName() {
        return "tp";
    }

    @Override
    public String getDescription() {
        return "teleports you to a checkpoint";
    }

    @Override
    public String getUsage() {
        return "<label>";
    }
}
