package evilcraft.api.helpers;

import java.util.Random;

import evilcraft.api.Coordinate;

/**
 * Contains helper methods that do some common mathematical calculations.
 * @author immortaleeb
 *
 */
public class MathHelpers {
	
	private static final Random random = new Random();

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
	 * Get a random point inside a sphere in an efficient way.
	 * @param center The center coordinates of the sphere.
	 * @param radius The radius of the sphere.
	 * @return The coordinates of the random point.
	 */
	public static Coordinate getRandomPointInSphere(Coordinate center, int radius) {
	    Coordinate randomPoint = null;
	    while(randomPoint == null) {
	        int x = center.x - radius + random.nextInt(2 * radius);
	        int y = center.y - radius + random.nextInt(2 * radius);
	        int z = center.z - radius + random.nextInt(2 * radius);
	        int dx = center.x - x;
	        int dy = center.y - y;
	        int dz = center.z - z;
	        int distance = (int) Math.sqrt(dx * dx + dy * dy + dz * dz);
	        if(distance <= radius) {
	            randomPoint = new Coordinate(x, y, z);
	        }
	    }
	    return randomPoint;
	}

}
