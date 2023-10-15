package assignmentJava.exceptions;

/**
 * If a certain entity doesn't exist in a context where it's expected to exist,
 * this exception can be thrown.
 */
public class NonExistentEntityException extends Exception {
    public NonExistentEntityException(int uniqueID) {
        super("No entity exists with UID = " + uniqueID);
    }
}
