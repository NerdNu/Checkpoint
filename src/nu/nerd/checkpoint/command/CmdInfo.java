package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointException;
import nu.nerd.checkpoint.CheckpointPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdInfo extends CheckpointCommand {

    public CmdInfo(CheckpointPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new CheckpointException(null, true);
        }

        String name = args.get(0).toLowerCase();
        CheckpointCourse course = plugin.getCourse(name);
        StringBuilder builder = new StringBuilder();
        Location giver = course.getItemGiver();
        builder.append("Course name: {{").append(name).append("}}\n");
        if (course.getItem() != null) {
            builder.append("Teleporter item: {{").append(course.getItem().getType()).append("}}\n");
        }
        if (course.getItemGiver() != null) {
            builder.append("Teleporter giver: {{(")
                    .append(giver.getBlockX()).append(",")
                    .append(giver.getBlockY()).append(",")
                    .append(giver.getBlockZ()).append(")}} in {{")
                    .append(giver.getWorld().getName()).append("}}\n");
        }
        for (Checkpoint checkpoint : course) {
            Location block = checkpoint.getBlock();
            Location target = checkpoint.getTarget();
            builder.append("Checkpoint {{").append(checkpoint.getLabel()).append("}}: {{(")
                    .append(block.getBlockX()).append(",")
                    .append(block.getBlockY()).append(",")
                    .append(block.getBlockZ()).append(")}} in {{")
                    .append(block.getWorld().getName()).append("}} -> {{(")
                    .append(target.getX()).append(",")
                    .append(target.getY()).append(",")
                    .append(target.getZ()).append(")}} in {{")
                    .append(target.getWorld().getName()).append("}}\n");
        }

        return builder.toString();
    }

    @Override
    public String getName(){
        return "info";
    }

    @Override
    public String getDescription() {
        return "prints info about the course";
    }

    @Override
    public String getUsage(Player player) {
        return "<course>";
    }

}
