package nu.nerd.checkpoint.command.trigger;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.command.CheckpointCommand;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import nu.nerd.checkpoint.trigger.Trigger;

import java.util.List;
import java.util.Queue;

@DescribableMeta(
        name = "list",
        description = "lists all triggers for the selected course",
        usage = "[page]"
)
public class CmdTriggerList extends CheckpointCommand {

    private static final int TRIGGERS_PER_PAGE = 10;

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() > 1) {
            throw new UsageException(this);
        }


        CheckpointCourse course = player.getCourse();
        List<Trigger> triggers = course.getTriggers();
        int totalPages = (triggers.size() - 1) / TRIGGERS_PER_PAGE + 1;
        int page = 1;

        if (args.size() == 1) {
            String pageStr = args.poll();
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                throw new UsageException(this);
            }
        }

        if (page < 1 || page > totalPages) {
            throw new CheckpointException("Page number must be between 1 and " + totalPages);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Triggers for course {{").append(course.getName()).append("}} (")
                .append(page).append("/").append(totalPages).append("):");
        int startIndex = (page - 1) * TRIGGERS_PER_PAGE;
        int endIndex = Math.min(startIndex + TRIGGERS_PER_PAGE, triggers.size());
        for (int i = startIndex; i < endIndex; i++) {
            Trigger trigger = triggers.get(i);
            builder.append("\n").append(i).append(". ").append(trigger.toString());
        }
        return builder.toString();
    }

}
