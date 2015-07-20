package org.cyclops.evilcraft.core.helper;


/**
 * Contains helper methods that do some common mathematical calculations.
 * @author immortaleeb
 *
 */
public class MathHelpers {

	/**
	 * Normalize an angle around 180 degrees so that this value doesn't become too large/small.
	 * @param angle The angle to normalize.
	 * @return The normalized angle.
	 */
	public static float normalizeAngle_180(float angle) {
	    angle %= 360;
	    
	    while (angle <= -180)
	        angle += 360;
	    
	    while (angle > 180)
	        angle -= 360;
	    
	    return angle;
	}

    /**
     * Convert a double to a natural number taking into account a circular time counter
     * that is able to offset factor values if required.
     * It in fact converts a factor to bursts of natural numbers.
     * For example:
     *  value = 0,5; timing = 0 to 1
     *  value = 0,5; timing = 1 to 0
     *  value = 0,5; timing = 2 to 1
     *  ...
     * @param value The input value.
     * @param timing The timing value, should increment each time this is called for good results.
     * @return The bursted value.
     */
    public static int factorToBursts(double value, int timing) {
        boolean shouldHaveCost = true;
        if(value < 1) {
            int tickOffset = (int) Math.ceil(1 / value);
            value = Math.ceil(1 / (double) tickOffset);
            shouldHaveCost = timing % tickOffset == 0;
        }
        return shouldHaveCost ? (int) Math.ceil(value) : 0;
    }

}
