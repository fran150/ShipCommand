package ar.com.shipcommand.physics.magnitudes;

/**
 * Used to represent distances in various units
 */
public class Distance {
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
     * @param m Distance in meters
     */
    public Distance(double m) {
        current = m;
    }

    /**
     * Creates a new distance object
     * @param distance Distance magnitude
     * @param unit Unit of the specified distance
     */
    public Distance(double distance, DistanceUnits unit) {
        switch (unit) {
            case Meters:
                setMeters(distance);
                break;
            case Kilometers:
                setKilometers(distance);
                break;
            case NauticalMiles:
                setNauticalMiles(distance);
                break;
            case Yards:
                setYards(distance);
                break;
            case Feet:
                setFeet(distance);
                break;
        }
    }

    /**
     * Gets the current distance
     * @return Distance in meters
     */
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
     *
     * @return Distance in kilometers
     */
    public double inKilometers() {
        return current / 1000;
    }

    /**
     * Sets the current distance
     *
     * @param km Distance in kilometers
     */
    public void setKilometers(double km) {
        current = km * 1000;
    }

    /**
     * Gets the current distance
     *
     * @return Distance in nautical miles
     */
    public double inNauticalMiles() {
        return current / 1852;
    }

    /**
     * Sets the current distance
     *
     * @param nm Distance in nautical miles
     */
    public void setNauticalMiles(double nm) {
        current = nm * 1852;
    }

    /**
     * Gets the current distance
     *
     * @return Distance in yards
     */
    public double inYards() {
        return current * 1.094;
    }

    /**
     * Sets the current distance
     *
     * @param yards Distance in yards
     */
    public void setYards(double yards) {
        current = yards / 1.094;
    }

    /**
     * Gets the current distance
     *
     * @return Distance in feet
     */
    public double inFeet() {
        return current * 3.281;
    }

    /**
     * Sets the current distance
     *
     * @param ft Distance in feet
     */
    public void setFeet(double ft) {
        current = ft / 3.281;
    }
}
