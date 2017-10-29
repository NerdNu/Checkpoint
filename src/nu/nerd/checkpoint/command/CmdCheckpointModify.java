package nu.nerd.checkpoint.command;
public class CmdCheckpointModify extends DirectoryCommand {

    public CmdCheckpointModify() {
        super(
                new CmdCheckpointModifyOrder(),
                new CmdCheckpointModifyLocation(),
                new CmdCheckpointModifyIcon()
        );
    }

    @Override
    public String getName() {
        return "modify";
    }

    @Override
    public String getDescription() {
        return "modify a checkpoint";
    }

}
