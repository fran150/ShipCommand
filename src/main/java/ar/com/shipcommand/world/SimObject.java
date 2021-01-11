package ar.com.shipcommand.world;

import ar.com.shipcommand.main.GameObject;
import ar.com.shipcommand.physics.magnitudes.Distance;
import ar.com.shipcommand.physics.magnitudes.Speed;
import ar.com.shipcommand.geo.Geo3DPosition;

/**
 * Represents an object in the simulation world.
 *
 * It has a definite position and state (velocity and acceleration)
 * It inherits from game objects and on each time step it integrates the new position
 */
public abstract class SimObject implements GameObject {
    private Geo3DPosition position;
    private double course;
    private double diveAngle;
    private double turningSpeed;
    private double divingSpeed;

    private Speed speed;
    private Speed acceleration;

    private Speed verticalSpeed;
    private Speed horizontalSpeed;

    /**
     * Creates a new sim object on the default position
     */
    public SimObject() {
        position = new Geo3DPosition();
        course = 0.0;
        diveAngle = 0.0;
        turningSpeed = 0.0;
        divingSpeed = 0.0;

        speed = new Speed();
        acceleration = new Speed();

        horizontalSpeed = new Speed();
        verticalSpeed = new Speed();
    }

    /**
     * Creates a new sim object with the given parameters
     *
     * @param position Object's position
     * @param course Object's course degrees from north
     * @param speed Object's speed
     * @param diveAngle Object's dive angle in degrees +90 to -90
     * @param turningSpeed Object's turning speed in degrees per minute
     * @param divingSpeed Speed in witch the diving angle is changing in degrees per minute
     * @param acceleration Object's acceleration
     */
    public SimObject(Geo3DPosition position, double course, double diveAngle, double turningSpeed, double divingSpeed, Speed speed, Speed acceleration) {
        this.position = position;
        this.course = course;
        this.diveAngle = diveAngle;
        this.turningSpeed = turningSpeed;
        this.divingSpeed = divingSpeed;

        this.speed = speed;
        this.acceleration = acceleration;

        horizontalSpeed = new Speed();
        verticalSpeed = new Speed();
        updateDerivedParameters();
    }

    /**
     * Integrates the position given current speed and acceleration
     *
     * @param dt Time passed since last update
     */
    public void timeStep(double dt) {
        // Change the speed given the acceleration
        double speedMs = speed.inMetersPerSecond();
        speedMs += acceleration.inMetersPerSecond() * dt;
        speed.setMetersPerSecond(speedMs);

        // Change course given the current turning speed in degrees per minute
        setCourse((turningSpeed / 60) * dt);

        // Change the dive angle given the current diving speed
        setDiveAngle((divingSpeed / 60) * dt);

        // Update derived parameters
        updateDerivedParameters();

        // Get the distance traveled on the time step
        Distance traveled = new Distance(horizontalSpeed.inMetersPerSecond() * dt);
        // Move the current position the specified distance on the present course
        position.move(course, traveled);

        // Get the current altitude in meters
        Distance altitude = position.getAltitude();
        // Add the climbed meters to the current altitude
        altitude.setMeters(altitude.inMeters() + (verticalSpeed.inMetersPerSecond() * dt));
    }

    /**
     * Gets the current position of the object
     *
     * @return Object's position
     */
    public Geo3DPosition getPosition() {
        return position;
    }

    /**
     * Gets the object's current course
     *
     * @return Degrees from north
     */
    public double getCourse() {
        return course;
    }

    /**
     * Sets the object's current course
     *
     * @param course Degrees from north
     */
    public void setCourse(double course) {
        this.course = course;
    }

    /**
     * Sets the object's current dive angle
     *
     * @return Dive angle in degrees
     */
    public double getDiveAngle() {
        return diveAngle;
    }

    /**
     * Sets the object's dive angle
     *
     * @param diveAngle Dive angle -90 to +90
     */
    public void setDiveAngle(double diveAngle) {
        if (diveAngle > 90) diveAngle = 90;
        if (diveAngle < -90) diveAngle = -90;
        this.diveAngle = diveAngle;
    }

    /**
     * Gets the current turning speed of the object
     *
     * @return Current turning speed in degrees per minute
     */
    public double getTurningSpeed() {
        return turningSpeed;
    }

    /**
     * Sets the current turning speed of the object
     *
     * @param turningSpeed Turning speed in degrees per minute
     */
    public void setTurningSpeed(double turningSpeed) {
        this.turningSpeed = turningSpeed;
    }

    /**
     * Gets the object's rate of change in the diving angle
     *
     * @return Degrees per minute
     */
    public double getDivingSpeed() {
        return divingSpeed;
    }

    /**
     * Sets the object's rate of change in the diving angle
     *
     * @param divingSpeed Degrees per minute
     */
    public void setDivingSpeed(double divingSpeed) {
        this.divingSpeed = divingSpeed;
    }

    /**
     * Gets the object's current speed
     *
     * @return Object's speed
     */
    public Speed getSpeed() {
        return speed;
    }

    /**
     * Gets the object's acceleration
     *
     * @return Object's acceleration
     */
    public Speed getAcceleration() {
        return horizontalSpeed;
    }

    /**
     * Gets the object's current horizontal speed
     *
     * @return Object's horizontal speed
     */
    public Speed getHorizontalSpeed() {
        return horizontalSpeed;
    }

    /**
     * Gets the object's current vertical speed
     *
     * @return Object's vertical speed
     */
    public Speed getVerticalSpeed() {
        return verticalSpeed;
    }

    /**
     * Updates derived parameters like horizontal and vertical speed
     */
    protected void updateDerivedParameters() {
        double ms = speed.inMetersPerSecond();
        double angle = Math.toRadians(diveAngle);

        verticalSpeed.setMetersPerSecond(ms * Math.sin(angle));
        horizontalSpeed.setMetersPerSecond(ms * Math.cos(angle));
    }
}
