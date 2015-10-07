package evilcraft.api;

import evilcraft.api.tileentity.INBTSerializable;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Location interface.
 * @author rubensworks
 *
 */
public interface ILocation extends Comparable<ILocation>, INBTSerializable {

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
    public ISize getDifference(ILocation location);
    
    /**
     * Get the distance between this and the given location.
     * @param location The location to calculate the distance with.
     * @return The distance.
     */
    public int getDistance(ILocation location);
    
    /**
     * Subtract this location with a given location.
     * This will not change this location, it will create a new location object.
     * @param location The location to subtract with.
     * @return The subtraction result.
     * @throws IllegalArgumentException If the dimensions differ.
     */
    public ILocation subtract(ILocation location);

    /**
     * Add this location with a given location.
     * This will not change this location, it will create a new location object.
     * @param location The location to subtract with.
     * @return The subtraction result.
     * @throws IllegalArgumentException If the dimensions differ.
     */
    public ILocation add(ILocation location);

    /**
     * Add this location with a given direction.
     * This will not change this location, it will create a new location object.
     * @param direction The location to offset to.
     * @return The subtraction result.
     * @throws IllegalArgumentException If the dimensions differ.
     */
    public ILocation offset(ForgeDirection direction);
    
    /**
     * Copy this location.
     * @return A deep copy.
     */
    public ILocation copy();
    
}
