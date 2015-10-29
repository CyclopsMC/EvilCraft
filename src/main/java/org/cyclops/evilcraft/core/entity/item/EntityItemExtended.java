package org.cyclops.evilcraft.core.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * An extended version of the entity item.
 * @author rubensworks
 *
 */
public abstract class EntityItemExtended extends EntityItem {
    
    /**
     * New instance.
     * @param world The world.
     * @param original The original entity item.
     */
	public EntityItemExtended(World world, EntityItem original) {
        super(world, original.posX, original.posY, original.posZ, original.getEntityItem());
        setPickupDelay(40);
        motionX = original.motionX;
        motionY = original.motionY;
        motionZ = original.motionZ;
    }

    /**
     * New instance.
     * @param world The world.
     * @param x X
     * @param y Y
     * @param z Z
     * @param itemStack The item stack
     */
    public EntityItemExtended(World world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
        setPickupDelay(40);
    }

    /**
     * New instance.
     * @param world The world.
     * @param x X
     * @param y Y
     * @param z Z
     */
    public EntityItemExtended(World world, double x, double y, double z) {
        super(world, x, y, z);
        setPickupDelay(40);
    }

    /**
     * New instance.
     * @param world The world.
     */
    public EntityItemExtended(World world) {
        super(world);
        setPickupDelay(40);
    }
	
}
