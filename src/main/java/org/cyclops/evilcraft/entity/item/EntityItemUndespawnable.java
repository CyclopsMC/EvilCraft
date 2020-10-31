package org.cyclops.evilcraft.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.World;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.entity.item.EntityItemIndestructable;

/**
 * Entity item that can not despawn.
 * @author rubensworks
 *
 */
public class EntityItemUndespawnable extends EntityItemIndestructable {

	public EntityItemUndespawnable(World world, ItemEntity original) {
		super(RegistryEntries.ENTITY_ITEM_UNDESPAWNABLE, world, original);
	}

	public EntityItemUndespawnable(EntityType<? extends EntityItemUndespawnable> type, World world) {
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
