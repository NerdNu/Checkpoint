package nu.nerd.checkpoint.command.trigger;

import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.command.DirectoryCommand;

@DescribableMeta(
        name = "trigger",
        description = "manage triggers"
)
public class CmdTrigger extends DirectoryCommand {

    public CmdTrigger() {
        super(
                new CmdTriggerAdd(),
                new CmdTriggerRemove(),
                new CmdTriggerInfo(),
                new CmdTriggerList(),
                new CmdTriggerTriggers(),
                new CmdTriggerActions()
        );
    }

}
