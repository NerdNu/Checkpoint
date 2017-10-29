package nu.nerd.checkpoint.command;

public class CmdCheckpoint extends DirectoryCommand {

    public CmdCheckpoint() {
        super(
                new CmdCheckpointAdd(),
                new CmdCheckpointRemove(),
                new CmdCheckpointModify(),
                new CmdCheckpointInfo(),
                new CmdCheckpointTp(),
                new CmdCheckpointList(),
                new CmdCheckpointReload()
        );
    }

    @Override
    public String getName() {
        return "checkpoint";
    }

    @Override
    public String getDescription() {
        return "manage checkpoints";
    }

}
