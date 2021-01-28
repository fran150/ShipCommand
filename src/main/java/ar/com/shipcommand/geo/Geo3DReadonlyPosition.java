package ar.com.shipcommand.geo;

import ar.com.shipcommand.physics.magnitudes.ReadOnlyDistance;

/**
 * Geographical position with altitude
 */
public interface Geo3DReadonlyPosition extends Geo2DReadonlyPosition {
    /**
     * Gets the object altitude over sea level (negative for under sea level)
     * @return object altitude above MSN, negative numbers for below sea level
     */
    ReadOnlyDistance getAltitude();
}
