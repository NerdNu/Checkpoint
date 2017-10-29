package nu.nerd.checkpoint.exception;

import nu.nerd.checkpoint.command.CheckpointCommand;

public class UsageException extends CheckpointException {

    public CheckpointCommand command;

    public UsageException(CheckpointCommand command) {
        super();
        this.command = command;
    }

    public UsageException(CheckpointCommand command, String message) {
        super(message);
        this.command = command;
    }

}
