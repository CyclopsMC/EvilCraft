package evilcraft.core.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * An indestructible version of the {@link EntityItem}.
 * @author rubensworks
 *
 */
public abstract class EntityItemIndestructable extends EntityItem {
    
    /**
     * New instance.
     * @param world The world.
     * @param x X
     * @param y Y
     * @param z Z
     * @param itemStack The item stack to set.
     */
	public EntityItemIndestructable(World world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
        delayBeforeCanPickup = 40;
    }
	
	protected boolean isIndestructable() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(isIndestructable()) {
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
