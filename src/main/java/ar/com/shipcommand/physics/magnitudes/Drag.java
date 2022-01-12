package ar.com.shipcommand.physics.magnitudes;

/**
 * Object used to represent the force of drag of an object moving through a fluid
 */
public class Drag {
    private double density;
    private Speed speed;
    private double crossSection;
    private double coefficient;

    public double getDragForceInNewtons() {
        return 0.5 * density * Math.pow(speed.inMetersPerSecond(), 2) * crossSection * coefficient;
    }

    public double getTerminalVelocity(Mass mass, double forceInNewtons) {
        return Math.sqrt((2 * mass.inKilograms() * forceInNewtons) / (density * crossSection * coefficient));
    }

    public double getForceForTerminalVelocity(Speed terminalVelocity, Mass mass) {
        double dragForce = 0.5 * density * Math.pow(terminalVelocity.inMetersPerSecond(), 2) * crossSection * coefficient;
        return dragForce / mass.inKilograms();
    }
}
