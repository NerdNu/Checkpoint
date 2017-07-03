package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointException;
import nu.nerd.checkpoint.CheckpointPlugin;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdCreateCourse extends CheckpointCommand {

    public CmdCreateCourse(CheckpointPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new CheckpointException(null, true);
        }

        String name = args.get(0).toLowerCase();
        plugin.addCourse(name);
        return "Course {{" + name + "}} created.";
    }

    @Override
    public String getName() {
        return "createcourse";
    }

    @Override
    public String getDescription() {
        return "creates a new course";
    }

    @Override
    public String getUsage(Player player) {
        return "<course>";
    }

}
