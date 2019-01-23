package ar.com.shipcommand.world;

import ar.com.shipcommand.main.IGameObject;
import ar.com.shipcommand.physics.Distance;
import ar.com.shipcommand.physics.Speed;
import ar.com.shipcommand.physics.geo.Geo3DPosition;

public class SimObject implements IGameObject {

    private Geo3DPosition position;
    private double course;

    private Speed speed;
    private Speed verticalSpeed;
    private double turningSpeed;

    private Speed verticalAcceleration;
    private Speed horizontalAcceleration;


    public SimObject() {
        position = new Geo3DPosition();
        course = 0.0;

        speed = new Speed();
        verticalSpeed = new Speed();
        turningSpeed = 0.0;

        verticalAcceleration = new Speed();
        horizontalAcceleration = new Speed();
    }

    public SimObject(Geo3DPosition position, double course, Speed speed, Speed verticalSpeed) {
        this.position = position;
        this.course = course;

        this.speed = speed;
        this.verticalSpeed = verticalSpeed;
        turningSpeed = 0.0;

        verticalAcceleration = new Speed();
        horizontalAcceleration = new Speed();
    }

    public void timeStep(double dt) {
        double speedMs = speed.inMetersPerSecond();
        double verticalSpeedMs = verticalSpeed.inMetersPerSecond();

        // Change the speed given the acceleration
        speedMs += horizontalAcceleration.inMetersPerSecond() * dt;
        speed.setMetersPerSecond(speedMs);

        // Change the vertical speed given the vertical acceleration
        verticalSpeedMs += verticalAcceleration.inMetersPerSecond() * dt;
        verticalSpeed.setMetersPerSecond(verticalSpeedMs);

        // Get the distance traveled on the time step
        Distance traveled = new Distance(speedMs * dt);
        // Move the current position the specified distance on the present course
        position.move(traveled, course);

        // Get the current altitude in meters
        double currentAltMeters = position.getAltitude().inMeters();
        // Get the climbed meters on this time step
        double climbedMeters = verticalSpeedMs * dt;
        // Add the climbed meters to the current altitude
        position.getAltitude().setMeters(currentAltMeters + climbedMeters);

        // Change course given the current turning speed in degrees per minute
        course += (turningSpeed / 60) * dt;
    }
}
