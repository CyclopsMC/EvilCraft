package evilcraft.api;

/**
 * Helper class that can hold coordinates.
 * @author rubensworks
 *
 */
public class Coordinate {
    /**
     * X coordinate.
     */
    public int x;
    /**
     * Y coordinate.
     */
    public int y;
    /**
     * Z coordinate.
     */
    public int z;
    
    /**
     * Make a new coordinate in a three dimensional space.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public boolean equals(Object object) {
        if(object instanceof Coordinate) {
            Coordinate coordinate = (Coordinate) object;
            return coordinate.x == x && coordinate.y == y && coordinate.z == z;
        }
        return false;
    }
}
