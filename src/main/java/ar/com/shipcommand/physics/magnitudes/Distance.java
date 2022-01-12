package ar.com.shipcommand.physics.magnitudes;

import ar.com.shipcommand.physics.units.DistanceUnits;

/**
 * Used to represent distances in various units
 */
public class Distance implements ReadOnlyDistance {
    /**
     * Distance in meters
     */
    private double current;

    /**
     * Creates a new distance object
     */
    public Distance() {
        current = 0;
    }

    /**
     * Creates a new distance object
     * @param meters Distance in meters
     */
    public Distance(double meters) {
        current = meters;
    }

    /**
     * Creates a new distance object
     * @param distance Distance magnitude
     * @param unit Unit of the specified distance
     */
    public Distance(double distance, DistanceUnits unit) {
        switch (unit) {
            case meters:
                setMeters(distance);
                break;
            case kilometers:
                setKilometers(distance);
                break;
            case nauticalMiles:
                setNauticalMiles(distance);
                break;
            case yards:
                setYards(distance);
                break;
            case feet:
                setFeet(distance);
                break;
        }
    }

    /**
     * Clones the specified object
     * @param distance Object to clone
     */
    public Distance(Distance distance) {
        current = distance.inMeters();
    }

    /**
     * Gets the current distance
     * @return Distance in meters
     */
    @Override
    public double inMeters() {
        return current;
    }

    /**
     * Sets the current distance
     * @param m Distance in meters
     */
    public void setMeters(double m) {
        current = m;
    }

    /**
     * Gets the current distance
     * @return Distance in kilometers
     */
    @Override
    public double inKilometers() {
        return current / 1000;
    }

    /**
     * Sets the current distance
     * @param km Distance in kilometers
     */
    public void setKilometers(double km) {
        current = km * 1000;
    }

    /**
     * Gets the current distance
     * @return Distance in nautical miles
     */
    @Override
    public double inNauticalMiles() {
        return current / 1852;
    }

    /**
     * Sets the current distance
     * @param nm Distance in nautical miles
     */
    public void setNauticalMiles(double nm) {
        current = nm * 1852;
    }

    /**
     * Gets the current distance
     * @return Distance in yards
     */
    @Override
    public double inYards() {
        return current * 1.094;
    }

    /**
     * Sets the current distance
     * @param yards Distance in yards
     */
    public void setYards(double yards) {
        current = yards / 1.094;
    }

    /**
     * Gets the current distance
     * @return Distance in feet
     */
    @Override
    public double inFeet() {
        return current * 3.281;
    }

    /**
     * Sets the current distance
     * @param ft Distance in feet
     */
    public void setFeet(double ft) {
        current = ft / 3.281;
    }

    /**
     * Returns distance as a readonly magnitude
     * @return Read only distance
     */
    public ReadOnlyDistance asReadOnly() {
        return this;
    }
}
