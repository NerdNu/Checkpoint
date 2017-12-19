package nu.nerd.checkpoint.command.checkpoint;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import nu.nerd.checkpoint.trigger.Trigger;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Queue;

public class CmdCheckpointInfo extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new UsageException(this);
        }

        CheckpointCourse course = player.getCourse();
        String label = args.poll().toLowerCase();
        Checkpoint checkpoint = course.getCheckpoint(label);

        return formatCheckpoint(checkpoint);
    }

    private String formatCheckpoint(Checkpoint checkpoint) {
        CheckpointCourse course = checkpoint.getCourse();
        return "Info for checkpoint {{" + checkpoint.getLabel() + "}}:\n"
            + "Course name: {{" + course.getName() + "}}\n"
            + "Location: " + Utils.formatLocation(checkpoint.getLocation()) + "\n"
            + "Triggers: " + formatTriggers(checkpoint) + "\n"
            + "Icon: {{" + formatIcon(checkpoint.getIcon()) + "}}";
    }

    private String formatTriggers(Checkpoint checkpoint) {
        CheckpointCourse course = checkpoint.getCourse();
        List<Trigger> triggers = course.getTriggers();
        if (triggers.isEmpty()) {
            return "none";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < triggers.size(); i++) {
            Trigger trigger = triggers.get(i);
            if (trigger.referencesCheckpoint(checkpoint)) {
                builder.append("\n  ").append(i).append(". ").append(trigger.toString());
            }
        }
        return builder.toString();
    }

    private String formatIcon(ItemStack icon) {
        if (icon == null) {
            return "none";
        }

        return icon.getType().toString();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "prints info about the specified checkpoint";
    }

    @Override
    public String getUsage() {
        return "<label>";
    }

}
