package ar.com.shipcommand.geo.exceptions;

/**
 * Exception produced when a trigonometry operation has infinite solutions
 */
public class InfiniteSolutionsException extends Exception {
    /**
     * Creates an InfiniteSolutionsException object
     */
    public InfiniteSolutionsException() {
        super("The operation has infinite solutions");
    }
}
