package ar.com.shipcommand.geo;

import ar.com.shipcommand.physics.magnitudes.Bearing;
import ar.com.shipcommand.physics.magnitudes.Distance;

/**
 * Geographical position of an object
 */
public class Geo2DPosition {
    /**
     * Current latitude
     */
    private double lat;

    /**
     * Current longitude
     */
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
     * @return Current latitude in radians
     */
    public double getLatRadians() {
        return Math.toRadians(lat);
    }

    /**
     * Returns current longitude in radians
     * @return Current longitude in radians
     */
    public double getLonRadians() {
        return Math.toRadians(lon);
    }

    /**
     * Sets the object's current latitude. If the specified value exceeds
     * a valid value for the latitude the value is converted to an equivalent value.
     * @param lat Decimal Latitude
     */
    public void setLat(double lat) {
        double absLatitude = Math.abs(lat % 360);

        absLatitude = getLatitudeFromCircularAngle(absLatitude);

        if (lat >= 0) this.lat = absLatitude;
        if (lat < 0) this.lat = -absLatitude;
    }

    /**
     * Given a right handed angle return the correspondent latitude
     * @param degrees Degrees of latitude
     * @return Angular degree transformed to latitude
     */
    private double getLatitudeFromCircularAngle(double degrees) {
        if (degrees > 90 && degrees <= 180) return 180 - degrees;
        if (degrees > 180 && degrees <= 270) return degrees - 180;
        if (degrees > 270 && degrees <= 360) return 360 - degrees;

        return degrees;
    }

    /**
     * Sets the object's current longitude.  If the specified value exceeds
     * a valid value for the longitude the value is converted to an equivalent value.
     * @param lon Decimal Longitude
     */
    public void setLon(double lon) {
        double absLongitude = lon % 360;

        absLongitude = getLongitudeFromCircularAngle(absLongitude);

        this.lon = absLongitude;
    }

    /**
     * Given a right hand angle returns the correspondent longitude
     * @param degrees Degrees of longitude
     * @return Angular degrees transformed to longitude
     */
    private double getLongitudeFromCircularAngle(double degrees) {
        if (degrees > 180) return degrees - 360;
        if (degrees < -180) return degrees + 360;
        return degrees;
    }

    /**
     * Sets the current latitude in radians
     * @param lat Latitude in radians
     */
    public void setLatRadians(double lat) {
        setLat(Math.toDegrees(lat));
    }

    /**
     * Sets the current longitude in radians
     * @param lon Longitude in radians
     */
    public void setLonRadians(double lon) {
        setLon(Math.toDegrees(lon));
    }

    /**
     * Sets the current position
     * @param lat Decimal latitude
     * @param lon Decimal longitude
     */
    public void setPosition(double lat, double lon) {
        setLat(lat);
        setLon(lon);
    }

    /**
     * Sets the current position in radians
     * @param lat Latitude in radians
     * @param lon Longitude in radians
     */
    public void setPositionRadians(double lat, double lon) {
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
     * @param lat Decimal latitude
     * @param lon Decimal longitude
     */
    public Geo2DPosition(double lat, double lon) {
        setPosition(lat, lon);
    }

    /**
     * Creates a copy of the specified position
     * @param position Position to copy
     */
    public Geo2DPosition(Geo2DPosition position) {
        copy(position);
    }

    /**
     * Copies the specified position to this one
     * @param pos Position to copy
     */
    public void copy(Geo2DPosition pos) {
        this.lat = pos.getLat();
        this.lon = pos.getLon();
    }

    /**
     * Move the current position
     * @param course Course in degrees from north
     * @param distance Distance to move
     */
    public void move(Bearing course, Distance distance) {
        GeoTools.movePosition(this, course, distance);
    }

    /**
     * Move the current position towards a specific point
     * @param end Point where the position must be move
     * @param distance Distance to move
     */
    public void moveTowards(Geo2DPosition end, Distance distance) {
        GeoTools.moveTowards(this, end, distance);
    }
}
