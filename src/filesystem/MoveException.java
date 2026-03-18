package filesystem;

/**
 * Simple exception for moving errors
 */
public class MoveException extends RuntimeException {
    /**
     * Create a MoveException with given error message
     *
     * @param message Given error message
     */
    public MoveException(String message) {
        super(message);
    }
}