package nu.nerd.checkpoint;

public class CheckpointException extends RuntimeException {

    private String message;
    private boolean showUsage;

    public CheckpointException(String message, boolean showUsage) {
        this.message = message;
        this.showUsage = showUsage;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasMessage() {
        return message != null;
    }

    public boolean isShowUsage() {
        return showUsage;
    }

}
