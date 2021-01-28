package ar.com.shipcommand.geo.exceptions;

/**
 * Exception produced when a trigonometry operation has ambiguous solutions
 */
public class AmbiguousSolutionException extends Exception {
    /**
     * Creates an AmbiguousSolutionException object
     */
    public AmbiguousSolutionException() {
        super("The operation has ambiguous solutions");
    }
}
