package org.cyclops.evilcraft.api.degradation;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Objects that can execute {@link IDegradationEffect} should implement this.
 * @author rubensworks
 * @see IDegradationRegistry
 */
public interface IDegradable {
    
    /**
     * Get the center location of this degradable instance.
     * @return The center coordinates.
     */
    public BlockPos getLocation();
    /**
     * Get the radius of the area.
     * @return The radius.
     */
    public int getRadius();
    /**
     * Get the list of entities within the area defined by {@link IDegradable#getRadius()}.
     * @return The entities within the area.
     */
    public List<Entity> getAreaEntities();
    /**
     * Get the numerical representation of the stage of degradation.
     * 0 is no degradation, 1 is the maximum degradation.
     * @return The numerical degradation, a value from 0 to 1.
     */
    public double getDegradation();
    /**
     * Get the world in which the degradable instance is located.
     * @return The world.
     */
    public World getDegradationWorld();
    
}
