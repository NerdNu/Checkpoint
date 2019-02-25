package nu.nerd.checkpoint.action;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Map;
import java.util.Queue;

@DescribableMeta(
        name = "checkpoint",
        description = "teleports the player to a checkpoint",
        usage = "<label>"
)
public class CheckpointAction extends Action {

    private Checkpoint checkpoint;
    private boolean checkVisited;

    @Override
    public void execute(CheckpointPlayer player) {
        if (!checkVisited || player.hasCheckpoint(checkpoint)) {
            player.teleport(checkpoint, true);
        }
    }

    @Override
    public String getParams() {
        return checkpoint.getLabel() + ", check-visited: " + checkVisited;
    }

    @Override
    public boolean referencesCheckpoint(Checkpoint checkpoint) {
        return checkpoint == this.checkpoint;
    }

    @Override
    protected void loadFromConfig(CheckpointCourse course, Map<String, Object> section) throws CheckpointException {
        String label = Utils.getString(section, "checkpoint");
        if (label == null) {
            throw new CheckpointException("No checkpoint provided");
        }
        checkpoint = course.getCheckpoint(label);
        checkVisited = Utils.getBoolean(section, "check-visited", false);
    }

    @Override
    protected void loadFromCommand(CheckpointPlayer player, Queue<String> params) throws CheckpointException {
        if (params.size() != 1) {
            throw new UsageException(null);
        }

        CheckpointCourse course = player.getCourse();
        String label = params.poll();
        checkpoint = course.getCheckpoint(label);
        checkVisited = false;
    }

    @Override
    protected void saveToConfig(Map<String, Object> config) {
        config.put("checkpoint", checkpoint.getLabel());
        config.put("check-visited", checkVisited);
    }

}
