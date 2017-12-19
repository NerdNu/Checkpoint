package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.exception.CheckpointException;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public abstract class DirectoryCommand extends CheckpointCommand {

    private CheckpointCommand[] subcommands;
    private Map<String, CheckpointCommand> subcommandMap;

    public DirectoryCommand(CheckpointCommand... subcommands) {
        super();
        this.subcommands = subcommands;
        subcommandMap = new HashMap<>();
        for (CheckpointCommand subcommand : subcommands) {
            subcommand.setParent(this);
            subcommandMap.put(subcommand.getName(), subcommand);
            for (String alias : subcommand.getAliases()) {
                subcommandMap.put(alias, subcommand);
            }
        }
    }

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (!args.isEmpty()) {
            String subcommandName = args.poll();
            CheckpointCommand subcommand = subcommandMap.get(subcommandName);
            if (subcommand != null) {
                return subcommand.execute(player, args);
            }
        }
        printSubcommands(player);
        return "";
    }

    protected void printSubcommands(CheckpointPlayer player) {
        for (CheckpointCommand subcommand : subcommands) {
            subcommand.printHelp(player);
        }
    }

}
