package ar.com.shipcommand.physics.magnitudes;

/**
 * Used to represent bearing or courses
 */
public class Bearing implements ReadOnlyBearing {
    /**
     * Current bearing in degrees
     */
    private double current;

    /**
     * Creates a new bearing object pointing north
     */
    public Bearing() {
        current = 0.0;
    }

    /**
     * Creates a new bearing object
     * @param degrees Current bearing in degrees
     */
    public Bearing(double degrees) {
        setDegrees(degrees);
    }

    /**
     * Get the current bearing in degrees
     * @return Returns the current bearing in degrees
     */
    @Override
    public double getDegrees() {
        return current;
    }

    /**
     * Get the current bearing in radians
     * @return Returns the current bearing in radians
     */
    @Override
    public double getRadians() {
        return Math.toRadians(current);
    }

    /**
     * Sets the current bearing in degrees
     * @param degrees New bearing in degrees
     */
    public void setDegrees(double degrees) {
        current = degrees % 360;
    }

    /**
     * Set the current bearing in radians
     * @param radians New bearing in radians
     */
    public void setRadians(double radians) {
        setDegrees(Math.toDegrees(radians));
    }

    /**
     * Calculate the new bearing after turning port the specified amount of degrees
     * @param degrees Degrees to turn port
     */
    public void turnPort(double degrees) {
        setDegrees(current - degrees);
    }

    /**
     * Calculate the new bearing after turning starboard the specified amount of degrees
     * @param degrees Degrees to turn starboard
     */
    public void turnStarboard(double degrees) {
        setDegrees(current + degrees);
    }

    /**
     * Calculate the new bearing after turning the specified amount of degrees
     * @param degrees Degrees to turn, negative for port, positive for starboard
     */
    public void turn(double degrees) {
        setDegrees(current + degrees);
    }
}
