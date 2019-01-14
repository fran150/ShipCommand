package ar.com.shipcommand.geo;

/**
 * Geographical position of an object
 */
public class Geo2DPosition {
    double lat;
    double lon;

    /**
     * Get the current latitude
     * @return Decimal Latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Get the current longitude
     * @return Decimal Longitude
     */
    public double getLon() {
        return lon;
    }

    /**
     * Returns current latitude in radians
     *
     * @return Current latitude in radians
     */
    public double getLatRadians() {
        return Math.toRadians(lat);
    }

    /**
     * Returns current longitude in radians
     *
     * @return Current longitude in radians
     */
    public double getLonRadians() {
        return Math.toRadians(lon);
    }

    /**
     * Sets the object's current latitude
     *
     * @param lat Decimal Latitude
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Sets the object's current longitude
     *
     * @param lon Decimal Longitude
     */
    void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * Sets the current latitude in radians
     *
     * @param lat Latitude in radians
     */
    public void setLatRadians(double lat) {
        this.lat = Math.toDegrees(lat);
    }

    /**
     * Sets the current longitude in radians
     *
     * @param lon Longitude in radians
     */
    public void setLonRadians(double lon) {
        this.lon = Math.toDegrees(lon);
    }

    /**
     * Sets the current position
     *
     * @param lat Decimal latitude
     * @param lon Decimal longitude
     */
    public void setPosition(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Sets the current position in radians
     *
     * @param lat Latitude in radians
     * @param lon Longitude in radians
     */
    public void setPostionRadians(double lat, double lon) {
        this.lat = Math.toDegrees(lat);
        this.lon = Math.toDegrees(lon);
    }

    /**
     * Creates a geographical position at 0° lat - 0° lon
     */
    public Geo2DPosition() {
        lat = 0;
        lon = 0;
    }

    /**
     * Creates a geographical position at the given decimal coordinates
     *
     * @param lat Decimal latitude
     * @param lon Decimal longitude
     */
    public Geo2DPosition(double lat, double lon) {
        setPosition(lat, lon);
    }
}
