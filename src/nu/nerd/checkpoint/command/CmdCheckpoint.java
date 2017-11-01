package nu.nerd.checkpoint.command;

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

    @Override
    public String getName() {
        return "checkpoint";
    }

    @Override
    public String getDescription() {
        return "manage checkpoints";
    }

}
