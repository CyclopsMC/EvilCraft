package org.cyclops.evilcraft.core.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * An extension of {@link net.minecraft.entity.projectile.EntityThrowable} that now
 * also has an inner {@link ItemStack} that must be implemented.
 * @author rubensworks
 *
 */
public abstract class EntityThrowable extends net.minecraft.entity.projectile.EntityThrowable {
    
    /**
     * New instance.
     * @param world The world.
     */
    public EntityThrowable(World world)
    {
        super(world);
    }

    /**
     * New instance for a certain thrower.
     * @param world The world.
     * @param entity The thrower.
     */
    public EntityThrowable(World world, EntityLivingBase entity)
    {
        super(world, entity);
    }

    /**
     * New instance at a certain location.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    public EntityThrowable(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }
    
   /**
    * returns an item stack with the item damage and the throwable item 
    * associated with this entity
    * @return Get the inner {@link ItemStack}.
    */
    public abstract ItemStack getItemStack();
}
