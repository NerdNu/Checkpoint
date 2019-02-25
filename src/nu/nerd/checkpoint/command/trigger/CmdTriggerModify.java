package nu.nerd.checkpoint.command.trigger;

import nu.nerd.checkpoint.DescribableMeta;

@DescribableMeta(
        name = "modify",
        description = "modify a trigger"
)
public class CmdTriggerModify extends DirectoryCommand {

    public CmdTriggerModify() {
        super(
                new CmdCheckpointModifyOrder(),
                new CmdCheckpointModifyLocation(),
                new CmdCheckpointModifyIcon()
        );
    }

} {
}
