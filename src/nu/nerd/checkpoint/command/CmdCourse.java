package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.exception.CheckpointException;

import java.util.Queue;

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
    void printSubcommands(CheckpointPlayer player) {
        super.printSubcommands(player);
        printHelp(player);
    }

    @Override
    public String getName() {
        return "course";
    }

    @Override
    public String getDescription() {
        return "selects a course to use in subsequent commands";
    }

    @Override
    public String getUsage() {
        return "<course>";
    }

}
