package nu.nerd.checkpoint.command;

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

    @Override
    public String getName() {
        return "trigger";
    }

    @Override
    public String getDescription() {
        return "manage triggers";
    }

}
