package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.action.Action;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import nu.nerd.checkpoint.trigger.Trigger;

import java.util.Queue;

public class CmdTriggerInfo extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new UsageException(this);
        }

        CheckpointCourse course = player.getCourse();
        int index;
        try {
            index = Integer.parseInt(args.poll());
        } catch (NumberFormatException e) {
            throw new UsageException(this, "index must be an integer");
        }
        Trigger trigger = course.getTrigger(index);
        Action action = trigger.getAction();

        return "Info for trigger at index {{" + index + "}}:\n"
            + "Course name: {{" + course.getName() + "}}\n"
            + "Trigger type: {{" + trigger.getType() + " " + trigger.getParams() + "}}\n"
            + "Action: {{" + action.getType() + " " + action.getParams() + "}}";
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "prints info about the trigger at the specified index";
    }

    @Override
    public String getUsage() {
        return "<index>";
    }

}
