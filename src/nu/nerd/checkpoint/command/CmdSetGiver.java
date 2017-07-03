package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointException;
import nu.nerd.checkpoint.CheckpointPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class CmdSetGiver extends CheckpointCommand {

    public CmdSetGiver(CheckpointPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new CheckpointException(null, true);
        }

        String name = args.get(0).toLowerCase();

        Block target = player.getTargetBlock((Set<Material>) null, 10);
        if (target == null) {
            return "You must be looking at the block to set.";
        }
        Location location = target.getLocation();

        plugin.getCourse(name).setItemGiver(location);
        return "Teleporter item giver for course {{" + name + "}} set to {{(" + location.getBlockX() + "," +
                location.getBlockY() + "," + location.getBlockZ() + ")}} in {{" + location.getWorld().getName() + "}}.";
    }

    @Override
    public String getName(){
        return "setgiver";
    }

    @Override
    public String getDescription() {
        return "sets the teleporter item giver to the block you're looking at";
    }

    @Override
    public String getUsage(Player player) {
        return "<course>";
    }

}
