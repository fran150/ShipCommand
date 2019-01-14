package ar.com.shipcommand.geo.exceptions;

/**
 * Exception produced when a trigonometry operation has ambiguous solutions
 */
public class AmbiguousSolutionException extends Exception {
    /**
     * Default Constructor
     */
    public AmbiguousSolutionException() {
        super("The operations has ambiguous solutions");
    }
}
