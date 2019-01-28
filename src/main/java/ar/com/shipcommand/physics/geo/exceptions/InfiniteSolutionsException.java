package ar.com.shipcommand.physics.geo.exceptions;

/**
 * Exception produced when a trigonometry operation has infinite solutions
 */
public class InfiniteSolutionsException extends Exception {
    /**
     * Default constructor
     */
    public InfiniteSolutionsException() {
        super("The operation has infinite solutions");
    }
}
