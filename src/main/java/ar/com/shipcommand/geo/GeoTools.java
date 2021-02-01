package ar.com.shipcommand.geo;

import ar.com.shipcommand.common.CommonConstants;
import ar.com.shipcommand.geo.exceptions.AmbiguousSolutionException;
import ar.com.shipcommand.geo.exceptions.InfiniteSolutionsException;
import ar.com.shipcommand.physics.magnitudes.Bearing;
import ar.com.shipcommand.physics.magnitudes.Distance;

/**
 * Collection of tools to calculate geographical data
 * https://www.movable-type.co.uk/scripts/latlong.html
 */
public class GeoTools {
    /**
     * Returns the degrees of difference in longitude of the given points
     * @param left point to the left
     * @param right point to the right
     * @return Longitude difference between the two points
     */
    public static double getLongitudeDifference(Geo2DPosition left, Geo2DPosition right) {
        double leftLon = left.getLon();
        double rightLon = right.getLon();

        if (leftLon < rightLon) {
            return rightLon - leftLon;
        } else {
            return (180 - leftLon) + (180 + rightLon);
        }
    }

    /**
     * Returns the degrees of difference in latitude of the given points
     * @param upper upper point
     * @param lower lower point
     * @return Latitude difference between the two points
     */
    public static double getLatitudeDifference(Geo2DPosition upper, Geo2DPosition lower) {
        double upperLat = upper.getLat();
        double upperLon = upper.getLon();
        double lowerLat = lower.getLat();
        double lowerLon = lower.getLon();

        if (Math.abs(upperLon - lowerLon) < 180.0) {
            return Math.abs(upperLat - lowerLat);
        } else {
            return Math.abs(upperLat + lowerLat);
        }
    }

    /**
     * Get distance between two points
     * @param start Start position
     * @param end End position
     * @return Distance in meters between the given points
     */
    public static Distance getDistance(Geo2DPosition start, Geo2DPosition end) {
        double phi1 = start.getLatRadians();
        double phi2 = end.getLatRadians();

        double deltaPhi = Math.toRadians(end.getLat() - start.getLat());
        double deltaLambda = Math.toRadians(end.getLon() - start.getLon());

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                   Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return new Distance(CommonConstants.EARTH_RADIUS * c);
    }

    /**
     * Get the initial bearing between two points
     * @param start Start position
     * @param end End position
     * @return Bearing in degrees to from the start point to the end
     */
    public static double getBearing(Geo2DPosition start, Geo2DPosition end) {
        double phi1 = start.getLatRadians();
        double lambda1 = start.getLonRadians();
        double phi2 = end.getLatRadians();
        double lambda2 = end.getLonRadians();

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }

    /**
     * Returns a new position calculated given a start position, a bearing and a distance
     * @param start Start position
     * @param bearing Bearing to move
     * @param distance Distance in meters
     */
    public static void movePosition(Geo2DPosition start, Bearing bearing, Distance distance) {
        double phi = start.getLatRadians();
        double lambda = start.getLonRadians();

        double theta = bearing.getRadians();
        double delta = distance.inMeters() / CommonConstants.EARTH_RADIUS;

        double phi2 = Math.asin(Math.sin(phi) * Math.cos(delta) + Math.cos(phi) * Math.sin(delta) * Math.cos(theta));
        double lambda2 = lambda + Math.atan2(Math.sin(theta) * Math.sin(delta) * Math.cos(phi), Math.cos(delta) - Math.sin(phi) * Math.sin(phi2));

        start.setPositionRadians(phi2, lambda2);
    }

    /**
     * Returns a new position that is moved the specified distance in the direction of the
     * end position
     * @param start Starting position
     * @param end Destination position
     * @param distance Distance to move
     */
    public static void moveTowards(Geo2DPosition start, Geo2DReadonlyPosition end, Distance distance) {
        double phi1 = start.getLatRadians();
        double lambda1 = start.getLonRadians();
        double phi2 = end.getLatRadians();
        double lambda2 = end.getLonRadians();

        double cosPhi1 = Math.cos(phi1);
        double sinPhi1 = Math.sin(phi1);
        double cosPhi2 = Math.cos(phi2);

        double y = Math.sin(lambda2 - lambda1) * cosPhi2;
        double x = cosPhi1 * Math.sin(phi2) - sinPhi1 * cosPhi2 * Math.cos(lambda2 - lambda1);

        double theta = Math.atan2(y, x);
        double delta = distance.inMeters() / CommonConstants.EARTH_RADIUS;

        double cosDelta = Math.cos(delta);
        double sinDelta = Math.sin(delta);

        double phi3 = Math.asin(sinPhi1 * cosDelta + cosPhi1 * sinDelta * Math.cos(theta));
        double lambda3 = lambda1 + Math.atan2(Math.sin(theta) * sinDelta * cosPhi1, cosDelta - sinPhi1 * Math.sin(phi3));

        start.setPositionRadians(phi3, lambda3);
    }


    /**
     * Returns the intersection position of two paths given its initial positions and bearings
     * @param p1 Initial position of first path
     * @param bearing1 Bearing of first path
     * @param p2 Initial position of second path
     * @param bearing2 Bearing of second path
     * @return Position at intersection
     */
    public static Geo2DPosition intersection(Geo2DPosition p1, double bearing1, Geo2DPosition p2, double bearing2)
        throws AmbiguousSolutionException, InfiniteSolutionsException {
        // Input parameters
        double phi1 = p1.getLatRadians();
        double lambda1 = p1.getLonRadians();
        double theta13 = Math.toRadians(bearing1);

        double phi2 = p2.getLatRadians();
        double lambda2 = p2.getLonRadians();
        double theta23 = Math.toRadians(bearing2);

        double deltaPhi = phi2 - phi1;
        double deltaLambda = lambda2 - lambda1;

        // Intersection point
        double phi3, lambda3;

        // Angular dist. p1–p2
        double delta12 = 2 * Math.asin(
            Math.sqrt(Math.pow(Math.sin(deltaPhi / 2), 2) + Math.cos(phi1) * Math.cos(phi2) * Math.sin(Math.pow(deltaLambda / 2, 2)))
        );

        // Initial / final bearings between points 1 & 2
        double thetaA = Math.acos((Math.sin(phi2) - Math.sin(phi1) * Math.cos(delta12)) / (Math.sin(delta12) * Math.cos(phi1)));
        double thetaB = Math.acos((Math.sin(phi1) - Math.sin(phi2) * Math.cos(delta12)) / (Math.sin(delta12) * Math.cos(phi2)));

        double theta12;
        double theta21;

        if (Math.sin(lambda2 - lambda1) > 0) {
            theta12 = thetaA;
            theta21 = (2 * Math.PI) - thetaB;
        } else {
            theta12 = (2 * Math.PI) - thetaA;
            theta21 = thetaB;
        }

        // Angle p2 – p1 – p3
        double alpha1 = theta13 - theta12;
        // Angle p1 – p2 – p3
        double alpha2 = theta21 - theta23;

        // Infinite solutions
        if (Math.sin(alpha1) == 0 && Math.sin(alpha2) == 0) {
            throw new InfiniteSolutionsException();
        }

        // Ambiguous Solution
        if ((Math.sin(alpha1) * Math.sin(alpha2)) < 0) {
            throw new AmbiguousSolutionException();
        }

        // Angle p1 – p2 – p3
        double alpha3 = Math.acos(-Math.cos(alpha1) * Math.cos(alpha2) + Math.sin(alpha1) * Math.sin(alpha2) * Math.cos(delta12));

        // Angular dist. p1 – p3
        double delta13 = Math.atan2(
            Math.sin(delta12) * Math.sin(alpha1) * Math.sin(alpha2), Math.cos(alpha2) + Math.cos(alpha1) * Math.cos(alpha3)
        );

        // P3 latitude
        phi3 = Math.asin(Math.sin(phi1) * Math.cos(delta13) + Math.cos(phi1) * Math.sin(delta13) * Math.cos(theta13));

        // Longitude p1 – p3
        double deltaLambda13 = Math.atan2(
            Math.sin(theta13) * Math.sin(delta13) * Math.cos(phi1), Math.cos(delta13) - Math.sin(phi1) * Math.sin(phi3)
        );
        lambda3 = lambda1 + deltaLambda13;

        // Create new position
        Geo2DPosition intersect = new Geo2DPosition();
        intersect.setPositionRadians(phi3, lambda3);

        return intersect;
    }
}
