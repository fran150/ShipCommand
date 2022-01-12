package ar.com.shipcommand.physics.magnitudes;

import ar.com.shipcommand.physics.units.TimeUnits;
import lombok.Getter;

/**
 * Represents the acceleration of an object
 */
public class Acceleration {
    @Getter
    private final Speed speed;
    @Getter
    private final Time time;

    /**
     * Creates a new acceleration object
     */
    public Acceleration() {
        speed = new Speed();
        time = new Time(1);
    }

    /**
     * Creates a new acceleration object of the given speed variation over an unit of time
     * @param speed Speed variation over the specified unit of time
     * @param timeUnits Type of unit of time
     */
    public Acceleration(Speed speed, TimeUnits timeUnits) {
        this.speed = speed;
        this.time = new Time(1, timeUnits);
    }

    /**
     * Creates a new acceleration object of the given speed and time
     * @param speed Speed variation on the elapsed time
     * @param time Time elapsed
     */
    public Acceleration(Speed speed, Time time) {
        this.speed = speed;
        this.time = time;
    }

    /**
     * Returns the current acceleration in meters per second squared
     * @return Meters per second squared
     */
    public double inMetersPerSecondSquared() {
        return speed.inMetersPerSecond() * time.inSeconds();
    }

    /**
     * Derives the current acceleration from a mass and a force in newtons
     * @param mass Mass of the object
     * @param forceInNewtons Force applied in newtons
     */
    public void derivative(Mass mass, double forceInNewtons) {
        double acceleration = forceInNewtons / mass.inKilograms();
        speed.setMetersPerSecond(acceleration);
    }
}
