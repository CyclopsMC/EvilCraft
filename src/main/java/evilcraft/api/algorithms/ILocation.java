package evilcraft.api.algorithms;

/**
 * Location interface.
 * @author rubensworks
 *
 */
public interface ILocation {

    /**
     * Get the amount of dimensions this location is defined in.
     * @return The amount of dimensions.
     */
    public int getDimensions();
    
    /**
     * Get the coordinates for this location.
     * @return An array of coordinates of the dimension for this location.
     */
    public int[] getCoordinates();
    
    /**
     * Set the coordinates for this location.
     * @param coordinates The coordinates.
     */
    public void setCoordinates(int[] coordinates);
    
    /**
     * Get the difference between this and the given location.
     * @param location The location to calculate the difference with.
     * @return The difference.
     */
    public Size getDifference(ILocation location);
    
    /**
     * Get the distance between this and the given location.
     * @param location The location to calculate the distance with.
     * @return The distance.
     */
    public int getDistance(ILocation location);
    
    /**
     * Copy this location.
     * @return A deep copy.
     */
    public ILocation copy();
    
}
