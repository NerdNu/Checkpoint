package nu.nerd.checkpoint.action;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import org.bukkit.Location;

import java.util.Map;
import java.util.Queue;

public class SleepAction extends Action {
    @Override
    public void execute(CheckpointPlayer player) {
        Location location = player.getPlayer().getLocation();
        player.getPlayer().setBedSpawnLocation(location);
        player.message("Bed location set.");
    }

    @Override
    public String getType() {
        return "sleep";
    }

    @Override
    protected void loadFromConfig(CheckpointCourse course, Map<String, Object> section) throws CheckpointException {
    }

    @Override
    protected void loadFromCommand(CheckpointPlayer player, Queue<String> params) throws CheckpointException {
        if (!params.isEmpty()) {
            throw new UsageException(null);
        }
    }

}
