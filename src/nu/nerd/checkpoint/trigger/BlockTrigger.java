package nu.nerd.checkpoint.trigger;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.exception.CheckpointException;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Map;
import java.util.Queue;

@DescribableMeta(
        name = "block",
        description = "triggered when a player interacts with the block you're looking at"
)
public class BlockTrigger extends Trigger {

    private Location location;

    @Override
    public String getParams() {
        return Utils.formatLocation(location);
    }

    /**
     * Returns the location of the block the trigger is listening to.
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    @Override
    protected void loadFromConfig(CheckpointCourse course, Map<String, Object> section) throws CheckpointException {
        Map<String, Object> locationSection = Utils.getSection(section, "location");
        if (locationSection == null) {
            throw new CheckpointException("No location provided");
        }
        location = Location.deserialize(locationSection);
    }

    @Override
    protected void loadFromCommand(CheckpointPlayer player, Queue<String> params) throws CheckpointException {
        try {
            Block targetBlock = player.getPlayer().getTargetBlock(null, 10);
            location = targetBlock.getLocation();
        } catch (IndexOutOfBoundsException e) {
            throw new CheckpointException("You must be looking at a block");
        }
    }

    @Override
    protected void saveToConfig(Map<String, Object> config) {
        config.put("location", location.serialize());
    }

}
