package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointException;
import nu.nerd.checkpoint.CheckpointPlugin;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdReload extends CheckpointCommand {

    public CmdReload(CheckpointPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws CheckpointException {
        plugin.loadFromConfig();

        return "Configuration reloaded.";
    }

    @Override
    public String getName(){
        return "reload";
    }

    @Override
    public String getDescription() {
        return "reloads the plugin configuration";
    }

    @Override
    public String getUsage(Player player) {
        return "";
    }

}
