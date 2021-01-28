package ar.com.shipcommand.geo;

/**
 * Geographical position of an object
 */
public interface Geo2DReadonlyPosition {
    /**
     * Get the current latitude
     * @return Decimal Latitude
     */
    double getLat();
    /**
     * Get the current longitude
     * @return Decimal Longitude
     */
    double getLon();
    /**
     * Get the current latitude in radians
     * @return Latitude in radians
     */
    double getLatRadians();
    /**
     * Get the current longitude in radians
     * @return Longitude in radians
     */
    double getLonRadians();
}
