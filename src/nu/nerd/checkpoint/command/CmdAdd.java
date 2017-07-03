package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointException;
import nu.nerd.checkpoint.CheckpointPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class CmdAdd extends CheckpointCommand {

    public CmdAdd(CheckpointPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws CheckpointException {
        if (args.size() < 5 || args.size() > 6) {
            throw new CheckpointException(null, true);
        }

        String name = args.get(0).toLowerCase();
        CheckpointCourse course = plugin.getCourse(name);

        String label = args.get(1).toLowerCase();

        Block block = player.getTargetBlock((Set<Material>) null, 10);
        if (block == null) {
            return "You must be looking at the block to set.";
        }
        Location blockLoc = block.getLocation();

        double x, y, z;
        try {
            x = Double.parseDouble(args.get(2));
            y = Double.parseDouble(args.get(3));
            z = Double.parseDouble(args.get(4));
        } catch (IllegalArgumentException e) {
            throw new CheckpointException(null, true);
        }

        World world = player.getWorld();
        if (args.size() == 6) {
            world = plugin.getServer().getWorld(args.get(5));
            return "No world with name {{" + args.get(5) + "}} exists.";
        }

        Location targetLoc = new Location(world, x, y, z);

        Checkpoint checkpoint = course.addCheckpoint(label, blockLoc, targetLoc);
        if (checkpoint == null) {
            return "A checkpoint with label {{" + label + "}} already exists.";
        }

        return "Checkpoint {{" + label + "}} created for course {{" + name + "}}.";
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "adds a checkpoint to the specified coordinates set by the block you're looking at";
    }

    @Override
    public String getUsage(Player player) {
        return "<course> <label> <x> <y> <z> [<world>]";
    }

}
