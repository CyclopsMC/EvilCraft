package evilcraft.core.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
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
     * @param original The original item entity
     */
	public EntityItemIndestructable(World world, EntityItem original) {
        super(world, original);
    }
	
	/**
	 * Make a new instance.
	 * @param world The world.
	 */
	public EntityItemIndestructable(World world) {
        super(world);
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
    }
	
	protected boolean isIndestructable() {
		return true;
	}
	
	protected boolean isUndespawnable() {
		return isIndestructable();
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(isUndespawnable()) {
			this.age--;
		}
	}
	
	@Override
	protected void dealFireDamage(int damage) {
		if(!isIndestructable()) {
			super.dealFireDamage(damage);
		}
    }
	
	@Override
	public boolean isEntityInvulnerable() {
		return isIndestructable();
	}
}
