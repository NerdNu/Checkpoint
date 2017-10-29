package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.Checkpoint;
import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import org.bukkit.Location;

import java.util.Queue;

public class CmdCheckpointList extends CheckpointCommand {

    private static final int CHECKPOINTS_PER_PAGE = 10;

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() > 1) {
            throw new UsageException(this);
        }


        CheckpointCourse course = player.getCourse();
        int totalPages = (course.checkpointCount() - 1) / CHECKPOINTS_PER_PAGE + 1;
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
        builder.append("Checkpoints for course {{").append(course.getName()).append("}} (")
                .append(page).append("/").append(totalPages).append("):");
        int i = (page - 1) * CHECKPOINTS_PER_PAGE;
        for (Checkpoint checkpoint
                : course.paginateCheckpoints((page - 1) * CHECKPOINTS_PER_PAGE, CHECKPOINTS_PER_PAGE)) {
            String label = checkpoint.getLabel();
            Location location = checkpoint.getLocation();
            builder.append("\n").append(i).append(". {{").append(label).append("}} at ")
                    .append(Utils.formatLocation(location));
            i++;
        }
        return builder.toString();
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "lists all checkpoints for the selected course";
    }

    @Override
    public String getUsage() {
        return "[page]";
    }

}
