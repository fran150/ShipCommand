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
        double l = Math.abs(lat % 360);
        if (l > 90 && l <= 180) l = 180 - l;
        if (l > 180 && l <= 270) l = lat - 180;
        if (l > 270 && l <= 360) l = 360 - lat;

        if (lat > 0) this.lat = l;
        if (lat < 0) this.lat = -l;
    }

    /**
     * Sets the object's current longitude
     *
     * @param lon Decimal Longitude
     */
    public void setLon(double lon) {
        double l = lon % 360;
        if (l > 180) l -= 360;
        if (l < -180) l += 360;
        this.lon = l;
    }

    /**
     * Sets the current latitude in radians
     *
     * @param lat Latitude in radians
     */
    public void setLatRadians(double lat) {
        setLat(Math.toDegrees(lat));
    }

    /**
     * Sets the current longitude in radians
     *
     * @param lon Longitude in radians
     */
    public void setLonRadians(double lon) {
        setLon(Math.toDegrees(lon));
    }

    /**
     * Sets the current position
     *
     * @param lat Decimal latitude
     * @param lon Decimal longitude
     */
    public void setPosition(double lat, double lon) {
        setLat(lat);
        setLon(lon);
    }

    /**
     * Sets the current position in radians
     *
     * @param lat Latitude in radians
     * @param lon Longitude in radians
     */
    public void setPostionRadians(double lat, double lon) {
        setLatRadians(lat);
        setLonRadians(lon);
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
     * @param course Course in degrees from north
     * @param distance Distance to move
     */
    public void move(double course, Distance distance) {
        GeoTools.movePosition(this, course, distance);
    }

    /**
     * Move the current position towards a specific point
     *
     * @param end Point where the position must be move
     * @param distance Distance to move
     */
    public void moveTowards(Geo2DPosition end, Distance distance) {
        GeoTools.moveTowards(this, end, distance);
    }

}
