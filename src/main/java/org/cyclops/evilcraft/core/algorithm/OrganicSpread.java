package org.cyclops.evilcraft.core.algorithm;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

/**
 * An algorithm that is able to organically spread an object in an area.
 * @author rubensworks
 *
 */
public class OrganicSpread {

    private static Random random = new Random();

    private Level world;
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
    public OrganicSpread(Level world, int dimensions, int radius, IOrganicSpreadable spreadable) {
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
     * Perform one spreading tick so that one new blockState will be spread to.
     * @param startLocation The location to start spreading from.
     */
    public void spreadTick(BlockPos startLocation) {
        Vec3 newLocation = new Vec3(startLocation.getX(), startLocation.getY(), startLocation.getZ());
        BlockPos newLocationConcrete = BlockPos.containing(newLocation);

        // Safely get a random direction.
        Vec3 direction = getRandomDirection();
        int attempts = 10;
        while(!isBigEnough(direction) && attempts > 0) {
            direction = getRandomDirection();
            attempts--;
        }
        if(!isBigEnough(direction)) return;

        // Loop in that direction.
        while (getSpreadable().isDone(world, newLocationConcrete) && isInArea(startLocation, newLocationConcrete)) {
            newLocation = newLocation.add(direction);
            newLocationConcrete = BlockPos.containing(newLocation);
        }

        // Spread to the new blockState.
        if (!getSpreadable().isDone(world, newLocationConcrete)) {
            getSpreadable().spreadTo(world, newLocationConcrete);
        }
    }

    protected boolean isInArea(BlockPos center, BlockPos location) {
        return Math.sqrt(center.distSqr(location)) <= getRadius();
    }

    protected boolean isBigEnough(Vec3 direction) {
        float MIN = 0.3F;
        return Math.abs(direction.x) >= MIN || Math.abs(direction.y) >= MIN || Math.abs(direction.z) >= MIN;
    }

    /**
     * Get a random direction to spread in.
     * @return An array of a choice of -1;0;1 per coordinate index.
     */
    protected Vec3 getRandomDirection() {
        return new Vec3((random.nextFloat() * 2 - 1) / 2, (random.nextFloat() * 2 - 1) / 2, (random.nextFloat() * 2 - 1) / 2);
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
        public boolean isDone(Level world, BlockPos location);
        /**
         * Spread to a given location.
         * @param world The world.
         * @param location The location.
         */
        public void spreadTo(Level world, BlockPos location);

    }

}
