package nu.nerd.checkpoint.action;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.CheckpointPlugin;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Map;
import java.util.Queue;

@DescribableMeta(
        name = "last-checkpoint",
        description = "teleports the player to the last checkpoint in the course they visited"
)
public class LastCheckpointAction extends Action {

    private CheckpointCourse course;

    @Override
    public void execute(CheckpointPlayer player) {
        Checkpoint checkpoint = player.getLastCheckpoint(course);
        if (checkpoint == null) {
            player.message("You have no checkpoints set.");
        } else {
            player.teleport(checkpoint, true);
        }
    }

    @Override
    protected void loadFromConfig(CheckpointCourse course, Map<String, Object> section) throws CheckpointException {
        this.course = course;
    }

    @Override
    protected void loadFromCommand(CheckpointPlayer player, Queue<String> params) throws CheckpointException {
        if (!params.isEmpty()) {
            throw new UsageException(null);
        }

        course = player.getCourse();
    }

}
