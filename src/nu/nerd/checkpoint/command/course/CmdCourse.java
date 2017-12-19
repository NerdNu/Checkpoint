package nu.nerd.checkpoint.command.course;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.command.DirectoryCommand;
import nu.nerd.checkpoint.exception.CheckpointException;

import java.util.Queue;

@DescribableMeta(
        name = "course",
        description = "selects a course to use in subsequent commands",
        usage = "<course>"
)
public class CmdCourse extends DirectoryCommand {

    public CmdCourse() {
        super(
                new CmdCourseAdd(),
                new CmdCourseRemove(),
                new CmdCourseList()
        );
    }

    @Override
    public String execute(CheckpointPlayer player, Queue<String> args) throws CheckpointException {
        if (args.size() == 1) {
            String courseName = args.peek();
            try {
                CheckpointCourse course = plugin.getCourse(courseName);
                player.setCourse(course);
                return "Course {{" + course.getName() + "}} selected.";
            } catch (CheckpointException e) {
                // No course found, might be a subcommnd
            }
        }
        return super.execute(player, args);
    }

    @Override
    protected void printSubcommands(CheckpointPlayer player) {
        super.printSubcommands(player);
        printHelp(player);
    }

}
