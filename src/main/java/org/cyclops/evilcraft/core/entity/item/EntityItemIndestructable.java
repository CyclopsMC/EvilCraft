package org.cyclops.evilcraft.core.entity.item;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

/**
 * An indestructible version of the {@link ItemEntity}.
 * Make sure to register sub-classes of this as a mod entity!
 * @author rubensworks
 *
 */
public abstract class EntityItemIndestructable extends EntityItemExtended {

    public EntityItemIndestructable(EntityType<? extends EntityItemIndestructable> type, Level world, ItemEntity original) {
        super(type, world, original);
        init();
    }

    public EntityItemIndestructable(EntityType<? extends EntityItemIndestructable> type, Level world) {
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
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return isIndestructable() || super.isInvulnerableTo(source);
    }

}
