package org.cyclops.evilcraft.core.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * An indestructible version of the {@link ItemEntity}.
 * Make sure to register sub-classes of this as a mod entity!
 * @author rubensworks
 *
 */
public abstract class EntityItemIndestructable extends EntityItemExtended {

	public EntityItemIndestructable(EntityType<? extends EntityItemIndestructable> type, World world, ItemEntity original) {
        super(type, world, original);
        init();
    }

	public EntityItemIndestructable(EntityType<? extends EntityItemIndestructable> type, World world) {
        super(type, world);
        init();
    }
    
    private void init() {
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
    public boolean isInvulnerableTo(DamageSource source) {
        return isIndestructable() || super.isInvulnerableTo(source);
    }

}
