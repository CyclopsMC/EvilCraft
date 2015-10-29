package org.cyclops.evilcraft.core.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * An indestructible version of the {@link EntityItem}.
 * Make sure to register sub-classes of this as a mod entity!
 * @author rubensworks
 *
 */
public abstract class EntityItemIndestructable extends EntityItemExtended {
    
    /**
     * New instance.
     * @param world The world.
     * @param original The original entity item.
     */
	public EntityItemIndestructable(World world, EntityItem original) {
        super(world, original);
        motionX = original.motionX;
        motionY = original.motionY;
        motionZ = original.motionZ;
        init();
    }
	
	/**
	 * Make a new instance.
	 * @param world The world.
	 */
	public EntityItemIndestructable(World world) {
        super(world);
        init();
    }

	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X
	 * @param y Y
	 * @param z Z
	 */
    public EntityItemIndestructable(World world, double x, double y, double z) {
        super(world, x, y, z);
        init();
    }
    
    /**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X
	 * @param y Y
	 * @param z Z
     * @param itemStack The item stack.
	 */
    public EntityItemIndestructable(World world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
        init();
    }
    
    private void init() {
        setPickupDelay(40);
        if(isUndespawnable()) {
            this.lifespan = Integer.MAX_VALUE;
        }
    }
	
	protected boolean isIndestructable() {
		return true;
	}
	
	protected boolean isUndespawnable() {
		return isIndestructable();
	}
	
	@Override
	protected void dealFireDamage(int damage) {
		if(!isIndestructable()) {
			super.dealFireDamage(damage);
		}
    }

	@Override
	public boolean isEntityInvulnerable(DamageSource damageSource) {
		return isIndestructable();
	}
}
