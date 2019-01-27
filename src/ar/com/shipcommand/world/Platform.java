package ar.com.shipcommand.world;

import ar.com.shipcommand.physics.AccelProfile;
import ar.com.shipcommand.physics.DragProfile;
import ar.com.shipcommand.physics.TurnProfile;
import ar.com.shipcommand.physics.magnitudes.Speed;

public abstract class Platform extends SimObject {
    private String name;
    protected String className = "Unknown";

    private double throttle;
    private double rudder;
    private double planes;

    private AccelProfile accelProfile;
    private DragProfile dragProfile;
    private TurnProfile turnProfile;
    private TurnProfile diveProfile;

    public Platform(String name) {
        this.name = name;

        this.accelProfile = new AccelProfile();
        this.dragProfile = new DragProfile();
        this.turnProfile = new TurnProfile();
        this.diveProfile = new TurnProfile();

        init();
    }

    protected abstract void init();

    public double getThrottle() {
        return throttle;
    }

    public void setThrottle(double throttle) {
        this.throttle = throttle;
    }

    public double getRudder() {
        return rudder;
    }

    public void setRudder(double rudder) {
        if (rudder > 1) rudder = 1;
        if (rudder < -1) rudder = -1;

        this.rudder = rudder;
    }

    public double getPlanes() {
        return planes;
    }

    public void setPlanes(short planes) {
        if (planes > 1) planes = 1;
        if (planes < -1) planes = -1;

        this.planes = planes;
    }

    public void timeStep(double dt) {
        Speed speed = getSpeed();
        Speed accel = getAcceleration();

        double speedMs = speed.inMetersPerSecond();;

        double currentMaxSpeedMs = accelProfile.getMaxSpeed().inMetersPerSecond() * throttle;

        if (speedMs < currentMaxSpeedMs) {
            accelProfile.interpolate(speed, accel, throttle);
        } else if (speedMs > currentMaxSpeedMs) {
            dragProfile.interpolate(speed, accel, throttle);
        } else {
            accel.setMetersPerSecond(0);
        }

        double turnSpeed = turnProfile.interpolate(speed) * rudder;
        setTurningSpeed(turnSpeed);

        double divingSpeed = diveProfile.interpolate(speed) * planes;
        setDivingSpeed(divingSpeed);

        super.timeStep(dt);
    }
}
