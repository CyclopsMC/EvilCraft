package evilcraft.api.algorithms;

import java.util.Random;

import net.minecraft.world.World;

/**
 * An algorithm that is able to organically spread an object in an area.
 * @author rubensworks
 *
 */
public class OrganicSpread {
    
    private static Random random = new Random();
    
    private World world;
    private int dimensions;
    private int radius;
    private IOrganicSpreadable spreadable;
    
    /**
     * Make a new instance.
     * @param world The world.
     * @param dimensions The amount of dimensions in which this spreading should happen.
     * @param radius The radius in which the spreading should happen.
     * @param spreadable The spreadable object.
     */
    public OrganicSpread(World world, int dimensions, int radius, IOrganicSpreadable spreadable) {
        this.world = world;
        this.setDimensions(dimensions);
        this.setRadius(radius);
        this.setSpreadable(spreadable);
    }
    
    /**
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }
    
    /**
     * @param radius the radius to set
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    /**
     * @return the dimensions
     */
    public int getDimensions() {
        return dimensions;
    }

    /**
     * @param dimensions the dimensions to set.
     */
    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }
    
    /**
     * @return the spreadable
     */
    public IOrganicSpreadable getSpreadable() {
        return spreadable;
    }

    /**
     * @param spreadable the spreadable to set
     */
    public void setSpreadable(IOrganicSpreadable spreadable) {
        this.spreadable = spreadable;
    }
    
    /**
     * Perform one spreading tick so that one new block will be spread to.
     * @param startLocation The location to start spreading from.
     */
    public void spreadTick(ILocation startLocation) {
        if(startLocation.getDimensions() != getDimensions()) {
            throw new RuntimeException("The dimensions of the given location do not equal "
                    + "this spreading dimensions.");
        }
        ILocation newLocation = startLocation.copy();
        
        // Safely get a random direction.
        float[] direction = getRandomDirection();
        int attempts = 10;
        while(!isBigEnough(direction) && attempts > 0) {
            direction = getRandomDirection();
            attempts--;
        }
        
        // Copy old coordinates to float array.
        float[] oldCoordinates = new float[newLocation.getDimensions()];
        for(int i = 0; i < newLocation.getDimensions(); i++) {
            oldCoordinates[i] = newLocation.getCoordinates()[i];
        }
        
        // Loop in that direction.
        while(getSpreadable().isDone(world, newLocation) && isInArea(startLocation, oldCoordinates)) {
            // Calculate the new coordinates
            float[] newCoordinates = new float[direction.length];
            for(int i = 0; i < newCoordinates.length; i++) {
                newCoordinates[i] = oldCoordinates[i] + direction[i];
            }
            
            // Set the new coordinates
            int[] finalCoordinates = new int[newLocation.getDimensions()];
            for(int i = 0; i < newLocation.getDimensions(); i++) {
                finalCoordinates[i] = (int) newCoordinates[i];
            }
            newLocation.setCoordinates(finalCoordinates);
            
            // Swap
            oldCoordinates = newCoordinates;
        }
        
        
        
        // Spread to the new block.
        if(!getSpreadable().isDone(world, newLocation)) {
            getSpreadable().spreadTo(world, newLocation);
        }
    }
    
    protected boolean isInArea(ILocation center, float[] location) {
        int distance = 0;
        for(int i = 0; i < center.getDimensions(); i++) {
            float d = center.getCoordinates()[i] - location[i];
            distance += d * d;
        }
        return Math.sqrt(distance) <= getRadius();
    }
    
    protected boolean isBigEnough(float[] direction) {
        float MIN = 0.3F;
        for(int i = 0; i < direction.length; i++) {
            if(direction[i] > MIN || direction[i] < MIN) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get a random direction to spread in.
     * @return An array of a choice of -1;0;1 per coordinate index.
     */
    protected float[] getRandomDirection() {
        float[] direction = new float[getDimensions()];
        for(int i = 0; i < direction.length; i++) {
            direction[i] = (random.nextFloat() * 2 - 1) / 2;
        }
        return direction;
    }
    
    

    /**
     * Interface for organically spreadable things.
     * @author rubensworks
     *
     */
    public interface IOrganicSpreadable {
        
        /**
         * Check if a certain location is already spread to.
         * @param world The world.
         * @param location The location.
         * @return If it is spread to.
         */
        public boolean isDone(World world, ILocation location);
        /**
         * Spread to a given location.
         * @param world The world.
         * @param location The location.
         */
        public void spreadTo(World world, ILocation location);
        
    }

}
