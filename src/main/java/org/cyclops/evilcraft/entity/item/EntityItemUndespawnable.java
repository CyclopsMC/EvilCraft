package org.cyclops.evilcraft.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.entity.item.EntityItemIndestructable;

/**
 * Entity item that can not despawn.
 * @author rubensworks
 *
 */
public class EntityItemUndespawnable extends EntityItemIndestructable {

    public EntityItemUndespawnable(Level world, ItemEntity original) {
        super(RegistryEntries.ENTITY_ITEM_UNDESPAWNABLE, world, original);
    }

    public EntityItemUndespawnable(EntityType<? extends EntityItemUndespawnable> type, Level world) {
        super(type, world);
    }

    @Override
    protected boolean isIndestructable() {
        return false;
    }

    @Override
    protected boolean isUndespawnable() {
        return true;
    }

}
