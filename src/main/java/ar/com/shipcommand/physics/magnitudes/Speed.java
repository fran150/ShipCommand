package ar.com.shipcommand.physics.magnitudes;

/**
 * Represents the velocity of an object
 */
public class Speed {
    private double current;

    /**
     * Creates an speed object
     */
    public Speed() {
        current = 0;
    }

    /**
     * Creates an speed object
     * @param kmh current speed in kilometers per hour
     */
    public Speed(double kmh) {
        current = kmh;
    }

    /**
     * Creates a new speed object
     * @param speed Current speed
     * @param unit Specified speed unit
     */
    public Speed(double speed, SpeedUnits unit) {
        switch (unit) {
            case KilometersPerHour:
                setKilometersPerHour(speed);
                break;
            case MetersPerSecond:
                setMetersPerSecond(speed);
                break;
            case Knots:
                setKnots(speed);
                break;
            case FeetPerMinute:
                setFeetPerMinute(speed);
                break;
        }
    }

    /**
     * Gets the current speed
     * @return speed in kilometers per hour
     */
    public double inKilometersPerHour() {
        return current;
    }

    /**
     * Sets the current speed
     * @param kmh speed in kilometers per hour
     */
    public void setKilometersPerHour(double kmh) {
        current = kmh;
    }

    /**
     * Gets ths current speed
     * @return Speed in meters per second
     */
    public double inMetersPerSecond() {
        return (current * 1000) / 3600;
    }

    /**
     * Sets the current speed
     * @param ms Speed in meters per second
     */
    public void setMetersPerSecond(double ms) {
        current = (ms / 1000) * 3600;
    }

    /**
     * Gets the current speed
     * @return Speed in knots
     */
    public double inKnots() {
        return current / 1.852;
    }

    /**
     * Sets the current speed
     * @param kt Speed in knots
     */
    public void setKnots(double kt) {
        current = kt * 1.852;
    }

    /**
     * Gets the current speed
     * @return Speed in feet per minute
     */
    public double inFeetPerMinute() {
        return current * ((3.281 * 1000) / 60);
    }

    /**
     * Sets the current speed
     * @param fpm Speed in feet per minute
     */
    public void setFeetPerMinute(double fpm) {
        current = fpm / ((3.281 * 1000) / 60);
    }
}
