package evilcraft.api.algorithms;

/**
 * Location class.
 * @author rubensworks
 *
 */
public class Location implements ILocation {

    private int dimensions;
    private int[] coordinates;
    
    /**
     * Make a new instance.
     * @param coordinates The coordinates.
     */
    public Location(int[] coordinates) {
        this.dimensions = coordinates.length;
        this.coordinates = coordinates;
    }
    
    @Override
    public int getDimensions() {
        return this.dimensions;
    }

    @Override
    public int[] getCoordinates() {
        return coordinates;
    }

    @Override
    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public ILocation copy() {
        return new Location(coordinates.clone());
    }

    

}
