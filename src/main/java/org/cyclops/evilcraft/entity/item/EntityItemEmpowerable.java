package org.cyclops.evilcraft.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.evilcraft.core.entity.item.EntityItemIndestructable;
import org.cyclops.evilcraft.item.IItemEmpowerable;

/**
 * Entity item for an {@link IItemEmpowerable}.
 * @author rubensworks
 *
 */
public class EntityItemEmpowerable extends EntityItemIndestructable {

	/**
     * New instance.
     * @param world The world.
     * @param original The original item entity
     */
	public EntityItemEmpowerable(World world, EntityItem original) {
        super(world, original);
    }
	
	/**
	 * Make a new instance.
	 * @param world The world.
	 */
	public EntityItemEmpowerable(World world) {
        super(world);
    }

	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X
	 * @param y Y
	 * @param z Z
	 */
    public EntityItemEmpowerable(World world, double x, double y, double z) {
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
    public EntityItemEmpowerable(World world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
    }
	
	@Override
	protected boolean isIndestructable() {
        if(!(getEntityItem().getItem() instanceof IItemEmpowerable)) return super.isIndestructable();
		return ((IItemEmpowerable) getEntityItem().getItem()).isEmpowered(getEntityItem());
	}
	
}
