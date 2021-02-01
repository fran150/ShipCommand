package ar.com.shipcommand.physics.magnitudes;

/**
 * Readonly object used to represent distances
 */
public interface ReadOnlyDistance {
    /**
     * Returns the current distance
     * @return Distance in meters
     */
    double inMeters();
    /**
     * Returns the current distance
     * @return Distance in kilometers
     */
    double inKilometers();
    /**
     * Returns the current distance
     * @return Distance in miles
     */
    double inNauticalMiles();
    /**
     * Returns the current distance
     * @return Distance in yards
     */
    double inYards();
    /**
     * Returns the current distance
     * @return Distance in feet
     */
    double inFeet();
}
