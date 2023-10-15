package assignmentJava.exceptions;

/**
 * If a duplicate entity already exists in a certain context where it is
 * undesirable to have two equal entities, this exception can be thrown.
 */
public class ExistingEntityException extends Exception {
    public ExistingEntityException(int uniqueID) {
        super(uniqueID + " exists already");
    }
}
