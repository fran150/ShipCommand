package ar.com.shipcommand.utils;

import ar.com.shipcommand.common.CommonConstants;

/**
 * Time handling utilities
 */
public class TimeUtils {
    /**
     * Gets the elapsed time in seconds
     * @param previousTime Nano time when the previous step started
     * @param currentTime Nano time the this step started
     * @return Difference in seconds for this time step
     */
    public static double getElapsedTime(long previousTime, long currentTime) {
        return (currentTime - previousTime) / CommonConstants.NANO_SECONDS_IN_A_SECOND;
    }

    /**
     * Returns true if more than a second has passed since the reference time
     * @param time Reference time in milliseconds
     * @return true if more than one second has passed from the given time
     */
    public static boolean aSecondHasPassedFrom(long time) {
        return System.currentTimeMillis() - time > CommonConstants.MILLISECONDS_IN_ONE_SECOND;
    }
}
