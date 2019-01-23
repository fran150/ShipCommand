package ar.com.shipcommand.physics.geo;

import ar.com.shipcommand.physics.Distance;
import ar.com.shipcommand.physics.geo.exceptions.AmbiguousSolutionException;
import ar.com.shipcommand.physics.geo.exceptions.InfiniteSolutionsException;

/**
 * Collection of tools to calculate geographical data
 *
 * https://www.movable-type.co.uk/scripts/latlong.html
 */
public class GeoTools {
    /**
     * Get distance between two points
     *
     * @param start Start position
     * @param end End position
     * @return Distance in meters between the given points
     */
    public static Distance getDistance(Geo2DPosition start, Geo2DPosition end) {
        double φ1 = start.getLatRadians();
        double φ2 = end.getLatRadians();

        double Δφ = Math.toRadians(end.getLat() - start.getLat());
        double Δλ = Math.toRadians(end.getLon() - start.getLon());


        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
                   Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return new Distance(GeoConsts.EARTH_RADIUS * c);
    }

    /**
     * Get the initial bearing between two points
     *
     * @param start Start position
     * @param end End position
     * @return Bearing in degrees to from the start point to the end
     */
    public static double getBearing(Geo2DPosition start, Geo2DPosition end) {
        double φ1 = start.getLatRadians();
        double λ1 = start.getLonRadians();
        double φ2 = end.getLatRadians();
        double λ2 = end.getLonRadians();

        double y = Math.sin(λ2 - λ1) * Math.cos(φ2);
        double x = Math.cos(φ1) * Math.sin(φ2) - Math.sin(φ1) * Math.cos(φ2) * Math.cos(λ2 - λ1);

        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns a new position calculated given a start position, a bearing and a distance
     *
     * @param start Start position
     * @param bearing Bearing in degrees
     * @param distance Distance in meters
     * @return New position calculated from the given parameters
     */
    public static Geo2DPosition movePosition(Geo2DPosition start, double bearing, Distance distance) {
        double φ = start.getLatRadians();
        double λ = start.getLonRadians();

        double θ = Math.toRadians(bearing);
        double δ = distance.inMeters() / GeoConsts.EARTH_RADIUS;


        double φ2 = Math.asin(Math.sin(φ)*Math.cos(δ) + Math.cos(φ) * Math.sin(δ) * Math.cos(θ));
        double λ2 = λ + Math.atan2(Math.sin(θ) * Math.sin(δ) * Math.cos(φ), Math.cos(δ) - Math.sin(φ) * Math.sin(φ2));

        start.setPostionRadians(φ2, λ2);

        return start;
    }

    /**
     * Returns the intersection position of two paths given its initial positions and bearings
     *
     * @param p1 Initial position of first path
     * @param bearing1 Bearing of first path
     * @param p2 Initial position of second path
     * @param bearing2 Bearing of second path
     * @return Position at intersection
     */
    public static Geo2DPosition intersection(Geo2DPosition p1, double bearing1, Geo2DPosition p2, double bearing2)
        throws AmbiguousSolutionException, InfiniteSolutionsException {
        // Input parameters
        double φ1 = p1.getLatRadians();
        double λ1 = p1.getLonRadians();
        double θ13 = Math.toRadians(bearing1);

        double φ2 = p2.getLatRadians();
        double λ2 = p2.getLonRadians();
        double θ23 = Math.toRadians(bearing2);

        double Δφ = φ2 - φ1;
        double Δλ = λ2 - λ1;

        // Intersection point
        double φ3, λ3;

        // Angular dist. p1–p2
        double δ12 = 2 * Math.asin(
            Math.sqrt(Math.pow(Math.sin(Δφ / 2), 2) + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Math.pow(Δλ / 2, 2)))
        );

        // Initial / final bearings between points 1 & 2
        double θa = Math.acos((Math.sin(φ2) - Math.sin(φ1) * Math.cos(δ12)) / (Math.sin(δ12) * Math.cos(φ1)));
        double θb = Math.acos((Math.sin(φ1) - Math.sin(φ2) * Math.cos(δ12)) / (Math.sin(δ12) * Math.cos(φ2)));

        double θ12;
        double θ21;

        if (Math.sin(λ2 - λ1) > 0) {
            θ12 = θa;
            θ21 = (2 * Math.PI) - θb;
        } else {
            θ12 = (2 * Math.PI) - θa;
            θ21 = θb;
        }

        // Angle p2 – p1 – p3
        double α1 = θ13 - θ12;
        // Angle p1 – p2 – p3
        double α2 = θ21 - θ23;

        // Infinite solutions
        if (Math.sin(α1) == 0 && Math.sin(α2) == 0) {
            throw new InfiniteSolutionsException();
        }

        // Ambiguous Solution
        if ((Math.sin(α1) * Math.sin(α2)) < 0) {
            throw new AmbiguousSolutionException();
        }

        // Angle p1 – p2 – p3
        double α3 = Math.acos(-Math.cos(α1) * Math.cos(α2) + Math.sin(α1) * Math.sin(α2) * Math.cos(δ12));

        // Angular dist. p1 – p3
        double δ13 = Math.atan2(
            Math.sin(δ12) * Math.sin(α1) * Math.sin(α2), Math.cos(α2) + Math.cos(α1) * Math.cos(α3)
        );

        // P3 latitude
        φ3 = Math.asin(Math.sin(φ1) * Math.cos(δ13) + Math.cos(φ1) * Math.sin(δ13) * Math.cos(θ13));

        // Longitude p1 – p3
        double Δλ13 = Math.atan2(
            Math.sin(θ13) * Math.sin(δ13) * Math.cos(φ1), Math.cos(δ13) - Math.sin(φ1) * Math.sin(φ3)
        );
        λ3 = λ1 + Δλ13;

        // Create new position
        Geo2DPosition intersect = new Geo2DPosition();
        intersect.setPostionRadians(φ3, λ3);

        return intersect;
    }
}
