package nu.nerd.checkpoint.command.trigger;

import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.Describable;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.trigger.Trigger;

import java.util.Queue;

@DescribableMeta(
        name = "triggers",
        description = "lists available trigger types"
)
public class CmdTriggerTriggers extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        StringBuilder builder = new StringBuilder("Available trigger types:");
        for (Class<? extends Trigger> triggerType: Trigger.TRIGGER_TYPES) {
            DescribableMeta meta = Describable.getMeta(triggerType);
            builder.append("\n{{").append(meta.name());
            if (!meta.usage().equals("")) {
                builder.append(" ").append(meta.usage());
            }
            builder.append("}} - ").append(meta.description());
        }
        return builder.toString();
    }

}
