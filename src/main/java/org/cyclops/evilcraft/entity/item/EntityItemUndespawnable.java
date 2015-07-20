package org.cyclops.evilcraft.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.evilcraft.core.entity.item.EntityItemIndestructable;

/**
 * Entity item that can not despawn.
 * @author rubensworks
 *
 */
public class EntityItemUndespawnable extends EntityItemIndestructable {

	/**
     * New instance.
     * @param world The world.
     * @param original The original item entity
     */
	public EntityItemUndespawnable(World world, EntityItem original) {
        super(world, original);
    }
	
	/**
	 * Make a new instance.
	 * @param world The world.
	 */
	public EntityItemUndespawnable(World world) {
        super(world);
    }

	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X
	 * @param y Y
	 * @param z Z
	 */
    public EntityItemUndespawnable(World world, double x, double y, double z) {
        super(world, x, y, z);
    }
    
    /**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X
	 * @param y Y
	 * @param z Z
     * @param itemStack The item stack.
	 */
    public EntityItemUndespawnable(World world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
    }
	
	@Override
	protected boolean isIndestructable() {
		return false;
	}
	
	@Override
	protected boolean isUndespawnable() {
		return true;
	}
	
}
