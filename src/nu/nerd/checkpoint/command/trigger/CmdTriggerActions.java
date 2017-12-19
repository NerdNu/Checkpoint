package nu.nerd.checkpoint.command.trigger;

import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.Describable;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.action.Action;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;

import java.util.Queue;

@DescribableMeta(
        name = "actions",
        description = "lists available action types"
)
public class CmdTriggerActions extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        StringBuilder builder = new StringBuilder("Available action types:");
        for (Class<? extends Action> actionType: Action.ACTION_TYPES) {
            DescribableMeta meta = Describable.getMeta(actionType);
            builder.append("\n{{").append(meta.name());
            if (!meta.usage().equals("")) {
                builder.append(" ").append(meta.usage());
            }
            builder.append("}} - ").append(meta.description());
        }
        return builder.toString();
    }

}
