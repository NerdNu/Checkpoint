package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import nu.nerd.checkpoint.trigger.Trigger;
import org.bukkit.Location;

import java.util.Queue;

public class CmdTriggerAdd extends CheckpointCommand {

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        CheckpointCourse course = player.getCourse();
        Trigger trigger;
        try {
            trigger = Trigger.deserialize(player, args);
        } catch (UsageException e) {
            e.command = this;
            throw e;
        }

        course.addTrigger(trigger);
        return "Trigger created for course {{" + course.getName() + "}}.";
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "adds a trigger";
    }

    @Override
    public String getUsage() {
        return "<trigger> <action>";
    }

}
