package nu.nerd.checkpoint.action;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;

import java.util.Map;
import java.util.Queue;

@DescribableMeta(
        name = "open-index",
        description = "allows the player to select from visited checkpoints"
)
public class OpenIndexAction extends Action {

    private CheckpointCourse course;

    @Override
    public void execute(CheckpointPlayer player) {
        player.openIndex(course);
    }

    @Override
    protected void loadFromConfig(CheckpointCourse course, Map<String, Object> section) {
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
