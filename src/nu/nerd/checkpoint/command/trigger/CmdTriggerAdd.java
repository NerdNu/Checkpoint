package nu.nerd.checkpoint.command.trigger;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import nu.nerd.checkpoint.trigger.Trigger;
import org.bukkit.Location;

import java.util.Queue;

@DescribableMeta(
        name = "add",
        description = "adds a trigger",
        usage = "<trigger> <action>"
)
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

}
