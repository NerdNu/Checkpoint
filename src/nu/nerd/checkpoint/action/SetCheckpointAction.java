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
        name = "set-checkpoint",
        description = "marks the player as having visited a checkpoint",
        usage = "<label> [visited]"
)
public class SetCheckpointAction extends Action {

    private Checkpoint checkpoint;
    private boolean visited;

    @Override
    public void execute(CheckpointPlayer player) {
        if (visited) {
            player.setCheckpoint(checkpoint);
            player.message("Checkpoint set.");
        } else {
            player.unsetCheckpoint(checkpoint);
            player.message("Checkpoint unset.");
        }
    }

    @Override
    public String getParams() {
        return checkpoint.getLabel() + ", visited=" + visited;
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
        visited = Utils.getBoolean(section, "visited", true);
    }

    @Override
    protected void loadFromCommand(CheckpointPlayer player, Queue<String> params) throws CheckpointException {
        if (params.size() < 1 || params.size() > 2) {
            throw new UsageException(null);
        }

        CheckpointCourse course = player.getCourse();
        String label = params.poll();
        checkpoint = course.getCheckpoint(label);
        visited = true;

        if (!params.isEmpty()) {
            try {
                visited = Boolean.parseBoolean(params.poll());
            } catch (IllegalArgumentException e) {
                throw new CheckpointException("visited must be true or false");
            }
        }
    }

    @Override
    protected void saveToConfig(Map<String, Object> config) {
        config.put("checkpoint", checkpoint.getLabel());
        config.put("visited", visited);
    }

}
