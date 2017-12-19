package nu.nerd.checkpoint.command.checkpoint;

import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;

import java.util.Queue;

@DescribableMeta(
        name = "reload",
        description = "reloads the plugin configuration"
)
public class CmdCheckpointReload extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        plugin.loadFromConfig();

        return "Configuration reloaded.";
    }

}
