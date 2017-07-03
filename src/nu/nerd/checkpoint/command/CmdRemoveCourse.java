package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointException;
import nu.nerd.checkpoint.CheckpointPlugin;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdRemoveCourse extends CheckpointCommand {

    public CmdRemoveCourse(CheckpointPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new CheckpointException(null, true);
        }

        String name = args.get(0).toLowerCase();
        plugin.removeCourse(name);
        return "Course {{" + name + "}} removed.";
    }

    @Override
    public String getName() {
        return "removecourse";
    }

    @Override
    public String getDescription() {
        return "removes an existing course";
    }

    @Override
    public String getUsage(Player player) {
        return "<course>";
    }

}
