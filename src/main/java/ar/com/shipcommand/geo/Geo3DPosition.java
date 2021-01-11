package ar.com.shipcommand.geo;

import ar.com.shipcommand.physics.magnitudes.Distance;

/**
 * Geographical position with altitude
 */
public class Geo3DPosition extends Geo2DPosition {
    /**
     * Altitude above sea level (or below if negative)
     */
    private final Distance altitude;

    /**
     * Gets the altitude over sea level
     * @return Altitude in meters
     */
    public Distance getAltitude() {
        return altitude;
    }

    /**
     * Creates a new 3D position at 0° lat, 0° lon, 0 meters
     */
    public Geo3DPosition() {
        super();
        altitude = new Distance();
    }

    /**
     * Creates a new 3D position
     * @param lat Current latitude
     * @param lon Current longitude
     * @param altitude Altitude in meters above sea level
     */
    public Geo3DPosition(double lat, double lon, double altitude) {
        super(lat, lon);
        this.altitude = new Distance(altitude);
    }

    /**
     * Creates a new 3D position
     * @param lat Current latitude
     * @param lon Current longitude
     * @param altitude Current altitude
     */
    public Geo3DPosition(double lat, double lon, Distance altitude) {
        super(lat, lon);
        this.altitude = altitude;
    }
}
