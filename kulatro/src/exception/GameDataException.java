package exception;

/**
 * This exception is a subclass of CardGameException, and is thrown when there is a data related exception, 
 * such as when a config file does not exist.
 * @author Muhammed Cagan Goktas
 */
public class GameDataException extends CardGameException {
    private static final long serialVersionUID = 102L;

    /**
	 * Constructs a new GameDataException with the specified detail message and cause.
	 * @param message the detail message explaining the cause of the exception
	 * @param cause the underlying exception or error that triggered this exception (e.g., FileNotFoundException)
	 */
	public GameDataException(String message, Throwable cause) {
        super(message + (cause != null ? " | Cause: " + cause.getMessage() : ""));
    }
}