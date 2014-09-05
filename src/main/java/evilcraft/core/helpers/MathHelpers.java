package evilcraft.core.helpers;


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

}
