package nu.nerd.checkpoint.action;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.CheckpointPlugin;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Queue;

@DescribableMeta(
        name = "checkpoint",
        description = "teleports the player to a checkpoint",
        usage = "<label>"
)
public class CheckpointAction extends Action {

    private Checkpoint checkpoint;

    @Override
    public void execute(CheckpointPlayer player) {
        CheckpointPlugin plugin = CheckpointPlugin.getInstance();
         new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(checkpoint);
            }
        }.runTask(plugin);
    }

    @Override
    public String getParams() {
        return checkpoint.getLabel();
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
    }

    @Override
    protected void loadFromCommand(CheckpointPlayer player, Queue<String> params) throws CheckpointException {
        if (params.size() != 1) {
            throw new UsageException(null);
        }

        CheckpointCourse course = player.getCourse();
        String label = params.poll();
        checkpoint = course.getCheckpoint(label);
    }

    @Override
    protected void saveToConfig(Map<String, Object> config) {
        config.put("checkpoint", checkpoint.getLabel());
    }

}
