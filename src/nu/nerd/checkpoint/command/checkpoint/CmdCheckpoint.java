package nu.nerd.checkpoint.command.checkpoint;

import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.command.DirectoryCommand;

@DescribableMeta(
        name = "checkpoint",
        description = "manage checkpoints"
)
public class CmdCheckpoint extends DirectoryCommand {

    public CmdCheckpoint() {
        super(
                new CmdCheckpointAdd(),
                new CmdCheckpointRemove(),
                new CmdCheckpointModify(),
                new CmdCheckpointVisit(),
                new CmdCheckpointTp(),
                new CmdCheckpointInfo(),
                new CmdCheckpointList(),
                new CmdCheckpointReload()
        );
    }

}
