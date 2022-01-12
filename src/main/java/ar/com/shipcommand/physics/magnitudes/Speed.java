package ar.com.shipcommand.physics.magnitudes;

import ar.com.shipcommand.physics.units.TimeUnits;
import lombok.Getter;

/**
 * Represent the speed of an object
 */
public class Speed {
    @Getter
    private final Distance distance;
    @Getter
    private final Time time;

    /**
     * Creates a new speed object
     */
    public Speed() {
        distance = new Distance(0);
        time = new Time(1);
    }

    /**
     * Creates a new speed object with the specified distance over unit of time
     * @param distance Distance covered in a unit of time
     * @param timeUnits Type of time unit
     */
    public Speed(Distance distance, TimeUnits timeUnits) {
        this.distance = distance;
        this.time = new Time(1, timeUnits);
    }

    /**
     * Creates a new speed object with the specified distance over time
     * @param distance Distance covered in the specified time
     * @param time time span of the magnitude
     */
    public Speed(Distance distance, Time time) {
        this.distance = distance;
        this.time = time;
    }

    /**
     * Returns the current speed in meters per second
     * @return Current speed in meters per second
     */
    public double inMetersPerSecond() {
        return distance.inMeters() / time.inSeconds();
    }

    /**
     * Sets the speed in meters per second
     * @param metersPerSecond Speed in meters per second
     */
    public void setMetersPerSecond(double metersPerSecond) {
        distance.setMeters(metersPerSecond);
        time.setSeconds(1);
    }

    /**
     * Returns the current speed in kilometers per hour
     * @return Current speed in kilometers per hour
     */
    public double inKilometersPerHour() {
        return distance.inKilometers() / time.inHours();
    }

    /**
     * Sets the current speed in kilometers per hour
     * @param kilometersPerHour Kilometers per hour
     */
    public void setKilometersPerHour(double kilometersPerHour) {
        distance.setKilometers(kilometersPerHour);
        time.setHours(1);
    }

    /**
     * Returns the current speed in knots
     * @return Current speed in knots
     */
    public double inKnots() {
        return distance.inNauticalMiles() / time.inHours();
    }

    /**
     * Sets the current speed in knots
     * @param knots Current speed in knots
     */
    public void setKnots(double knots) {
        distance.setNauticalMiles(knots);
        time.setHours(1);
    }

    /**
     * Returns the current speed in feet per minute
     * @return Current speed in feet per minute
     */
    public double inFeetPerMinute() {
        return distance.inFeet() / time.inMinutes();
    }

    /**
     * Sets the current speed in feet per minute
     * @param feetPerMinute Current speed in feet per minute
     */
    public void setFeetPerMinute(double feetPerMinute) {
        distance.setFeet(feetPerMinute);
        time.setMinutes(1);
    }

    /**
     * Calculates a new speed based on the given acceleration and elapsed time
     * @param acceleration Current acceleration
     * @param dt elapsed time in seconds
     */
    public void derivative(Acceleration acceleration, double dt) {
        double currentSpeed = inMetersPerSecond();
        currentSpeed += acceleration.inMetersPerSecondSquared() * dt;
        distance.setMeters(currentSpeed);
        time.setSeconds(1);
    }
}