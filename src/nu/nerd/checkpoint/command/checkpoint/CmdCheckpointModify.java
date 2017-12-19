package nu.nerd.checkpoint.command.checkpoint;

import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.command.DirectoryCommand;

@DescribableMeta(
        name = "modify",
        description = "modify a checkpoint"
)
public class CmdCheckpointModify extends DirectoryCommand {

    public CmdCheckpointModify() {
        super(
                new CmdCheckpointModifyOrder(),
                new CmdCheckpointModifyLocation(),
                new CmdCheckpointModifyIcon()
        );
    }

}
