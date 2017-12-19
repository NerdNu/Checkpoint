package nu.nerd.checkpoint.action;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import org.bukkit.Location;

import java.util.Map;
import java.util.Queue;

@DescribableMeta(
        name = "sleep",
        description = "sets the player's bed location to your current location"
)
public class SleepAction extends Action {

    private Location location;

    @Override
    public void execute(CheckpointPlayer player) {
        player.getPlayer().setBedSpawnLocation(location, true);
        player.message("Bed location set.");
    }

    @Override
    protected void loadFromConfig(CheckpointCourse course, Map<String, Object> section) throws CheckpointException {
        Map<String, Object> locationConfig = Utils.getSection(section, "location");
        if (locationConfig == null) {
            throw new CheckpointException("No location provided");
        }
        location = Location.deserialize(locationConfig);
    }

    @Override
    protected void loadFromCommand(CheckpointPlayer player, Queue<String> params) throws CheckpointException {
        if (!params.isEmpty()) {
            throw new UsageException(null);
        }

        location = player.getPlayer().getLocation();
    }

    @Override
    protected void saveToConfig(Map<String, Object> config) {
        config.put("location", location.serialize());
    }

}
