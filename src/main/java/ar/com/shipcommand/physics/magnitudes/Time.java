package ar.com.shipcommand.physics.magnitudes;

import ar.com.shipcommand.physics.units.TimeUnits;

/**
 * Immutable object used to represent time
 */
public class Time implements ReadOnlyTime {
    /**
     * Current time in seconds
     */
    private double current;

    /**
     * Creates a time with a value of 1 second
     */
    public Time() {
        current = 1;
    }

    /**
     * Creates a time object with the specified number of seconds
     * @param seconds Time in seconds
     */
    public Time(double seconds) {
        current = seconds;
    }

    /**
     * Creates a new time object with the specified time and unit
     * @param time Time magnitude
     * @param timeUnit Unit of the specified magnitude
     */
    public Time(double time, TimeUnits timeUnit) {
        switch (timeUnit) {
            case seconds:
                setSeconds(time);
                break;
            case minutes:
                setMinutes(time);
                break;
            case hours:
                setHours(time);
                break;
            case days:
                setDays(time);
                break;
        }
    }

    /**
     * Clones the specified time object
     * @param time Time object to clone
     */
    public Time(Time time) {
        current = time.inSeconds();
    }

    /**
     * Returns the current time in seconds
     * @return Current time in seconds
     */
    @Override
    public double inSeconds() {
        return current;
    }

    /**
     * Sets the time in seconds
     * @param seconds time in seconds
     */
    public void setSeconds(double seconds) {
        current = seconds;
    }

    /**
     * Returns the current time in minutes
     * @return Current time in minutes
     */
    @Override
    public double inMinutes() {
        return inSeconds() / 60;
    }

    /**
     * Sets the time in minutes
     * @param minutes time in minutes
     */
    public void setMinutes(double minutes) {
        setSeconds(minutes * 60);
    }

    /**
     * Returns the current time in hours
     * @return current time in hours
     */
    @Override
    public double inHours() {
        return inMinutes() / 60;
    }

    /**
     * Sets the time in hours
     * @param hours time in hours
     */
    public void setHours(double hours) {
        setMinutes(hours * 60);
    }

    /**
     * Returns the current time in days
     * @return current time in days
     */
    @Override
    public double inDays() {
        return inHours() / 24;
    }

    /**
     * Sets the time in days
     * @param days current time in days
     */
    public void setDays(double days) {
        setHours(days * 24);
    }

    /**
     * Returns the current time as a readonly magnitude
     * @return Read only time
     */
    public ReadOnlyTime asReadOnly() {
        return this;
    }
}
