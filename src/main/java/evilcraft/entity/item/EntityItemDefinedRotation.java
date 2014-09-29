package evilcraft.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.core.entity.item.EntityItemExtended;

/**
 * Entity item for an item that should have a defined rotation that does not change.
 * @author rubensworks
 *
 */
public abstract class EntityItemDefinedRotation extends EntityItemExtended {
	
	/**
     * New instance.
     * @param world The world.
     * @param original The original item entity
     */
	public EntityItemDefinedRotation(World world, EntityItem original) {
        super(world, original);
    }
	
	/**
	 * Make a new instance.
	 * @param world The world.
	 */
	public EntityItemDefinedRotation(World world) {
        super(world);
    }

	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X
	 * @param y Y
	 * @param z Z
	 */
    public EntityItemDefinedRotation(World world, double x, double y, double z) {
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
    public EntityItemDefinedRotation(World world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
    }
	
	protected abstract float getRotationYaw(World worldObj, double posX, double posY,
			double posZ);
	
	protected boolean hasCustomRotation() {
		return true;
	}
	
}
