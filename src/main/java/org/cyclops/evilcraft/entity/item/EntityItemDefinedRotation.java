package org.cyclops.evilcraft.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.core.entity.item.EntityItemExtended;

/**
 * Entity item for an item that should have a defined rotation that does not change.
 * @author rubensworks
 *
 */
public abstract class EntityItemDefinedRotation extends EntityItemExtended {

    public EntityItemDefinedRotation(EntityType<? extends EntityItemDefinedRotation> type, Level world, ItemEntity original) {
        super(type, world, original);
    }

    public EntityItemDefinedRotation(EntityType<? extends EntityItemDefinedRotation> type, Level world) {
        super(type, world);
    }

    protected boolean hasCustomRotation() {
        return true;
    }

    @Override
    public int getAge() {
        return hasCustomRotation() ? 0 : super.getAge();
    }
}
