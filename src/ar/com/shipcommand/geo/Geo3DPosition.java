package ar.com.shipcommand.geo;

/**
 * Geographical position with altitude
 */
public class Geo3DPosition extends Geo2DPosition {
    double altitude;

    /**
     * Gets the altitude over sea level
     *
     * @return Altitude in meters
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * Sets the altitude over sea level
     *
     * @param altitude Altitude in meters
     */
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    /**
     * Creates a position at 0° lat, 0° lon, 0 meters
     */
    public Geo3DPosition() {
        super();
        altitude = 0;
    }

    /**
     * Creates a position at the given position
     *
     * @param lat Current latitude
     * @param lon Current longitude
     * @param altitude Altitude in meters above sea level
     */
    public Geo3DPosition(double lat, double lon, double altitude) {
        super(lat, lon);

        this.altitude = altitude;
    }
}
