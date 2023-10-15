package assignmentJava.exceptions;

/**
 * If there is something wrong with an order that is being placed, or if the
 * order cannot be processed, then this exception can be thrown.
 */
public class InvalidOrderException extends Exception {
    public InvalidOrderException(String errorMessage) {
        super(errorMessage);
    }
}
