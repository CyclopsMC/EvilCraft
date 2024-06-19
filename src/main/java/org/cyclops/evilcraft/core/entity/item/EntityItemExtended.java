package org.cyclops.evilcraft.core.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * An extended version of the entity item.
 * @author rubensworks
 *
 */
public abstract class EntityItemExtended extends ItemEntity {

    public EntityItemExtended(EntityType<? extends EntityItemExtended> type, Level world) {
        super(type, world);
        this.setPickUpDelay(40);
    }

    public EntityItemExtended(EntityType<? extends EntityItemExtended> type, Level world, ItemEntity original) {
        super(type, world);
        this.setPickUpDelay(40);
        this.setDeltaMovement(original.getDeltaMovement());
        this.setPos(original.getX(), original.getY(), original.getZ());
        this.setItem(original.getItem());
    }

    // Needed for particle rendering
    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemEntity copy() {
        EntityItemExtended entity = (EntityItemExtended) getType().create(level());
        entity.setItem(this.getItem().copy());
        entity.copyPosition(this);
        //entity.age = this.getAge();
        //entity.hoverStart = this.hoverStart;
        return entity;
    }
}
