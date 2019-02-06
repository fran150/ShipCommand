package ar.com.shipcommand.physics.geo;

import ar.com.shipcommand.physics.magnitudes.Distance;

/**
 * Geographical position of an object
 */
public class Geo2DPosition {
    private double lat;
    private double lon;

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
        setPosition(lat, lon);
    }

    /**
     * Sets the object's current longitude
     *
     * @param lon Decimal Longitude
     */
    public void setLon(double lon) {
        setPosition(lat, lon);
    }

    /**
     * Sets the current latitude in radians
     *
     * @param lat Latitude in radians
     */
    public void setLatRadians(double lat) {
        setPostionRadians(lat, Math.toRadians(lon));
    }

    /**
     * Sets the current longitude in radians
     *
     * @param lon Longitude in radians
     */
    public void setLonRadians(double lon) {
        setPostionRadians(Math.toRadians(lat), lon);
    }

    /**
     * Sets the current position
     *
     * @param lat Decimal latitude
     * @param lon Decimal longitude
     */
    public void setPosition(double lat, double lon) {
        double newLon = (lon) % 360;
        if (newLon > 180)
            newLon -= 360;
        if (newLon < -180)
            newLon += 360;

        double newLat = (lat + 90) % 180;
        newLat = newLat - 90;

        this.lat = newLat;
        this.lon = newLon;
    }

    /**
     * Sets the current position in radians
     *
     * @param lat Latitude in radians
     * @param lon Longitude in radians
     */
    public void setPostionRadians(double lat, double lon) {
        setPosition(Math.toDegrees(lat), Math.toDegrees(lon));
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

    /**
     * Creates a copy of the current position
     *
     * @return new position
     */
    public Geo2DPosition clone() {
        return new Geo2DPosition(this.lat, this.lon);
    }

    /**
     * Copies the specified position to this one
     *
     * @param pos Position to copy
     */
    public void copy(Geo2DPosition pos) {
        this.lat = pos.getLat();
        this.lon = pos.getLon();
    }

    /**
     * Move the current position
     *
     * @param distance Distance to move
     * @param course Course in degrees from north
     */
    public void move(Distance distance, double course) {
        GeoTools.movePosition(this, course, distance);
    }

    public void moveLon(double degrees) {
        setLon(getLon() + degrees);
    }

    public void moveLat(double degrees) {
        setLat(getLat() + degrees);
    }
}
