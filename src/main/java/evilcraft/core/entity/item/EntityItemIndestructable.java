package evilcraft.core.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;

/**
 * An indestructible version of the {@link EntityItem}.
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
