package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.exception.CheckpointException;

import java.util.Queue;

public class CmdCheckpointReload extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
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

}
