package nu.nerd.checkpoint.exception;

public class CheckpointException extends Exception {

    private String message;

    public CheckpointException() {
        this.message = "";
    }

    public CheckpointException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasMessage() {
        return !message.isEmpty();
    }

    public CheckpointException prepend(String prefix) {
        if (hasMessage()) {
            message = prefix + ": " + message;
        } else {
            message = prefix;
        }
        return this;
    }

}
