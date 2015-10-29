package org.cyclops.evilcraft.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.evilcraft.core.entity.item.EntityItemExtended;

/**
 * Entity item for an item that should have a defined rotation that does not change.
 * @author rubensworks
 *
 */
public abstract class EntityItemDefinedRotation extends EntityItemExtended {
	
	/**
     * New instance.
     * @param world The world.
     * @param original The original entity item/
     */
	public EntityItemDefinedRotation(World world, EntityItem original) {
        super(world, original);
    }

	/**
	 * New instance.
	 * @param world The world.
	 * @param x X
	 * @param y Y
	 * @param z Z
	 * @param itemStack The item stack
	 */
	public EntityItemDefinedRotation(World world, double x, double y, double z, ItemStack itemStack) {
		super(world, x, y, z, itemStack);
	}

	/**
	 * New instance.
	 * @param world The world.
	 * @param x X
	 * @param y Y
	 * @param z Z
	 */
	public EntityItemDefinedRotation(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	/**
	 * New instance.
	 * @param world The world.
	 */
	public EntityItemDefinedRotation(World world) {
		super(world);
	}
	
	protected boolean hasCustomRotation() {
		return true;
	}
	
}
