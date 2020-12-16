package org.cyclops.evilcraft.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.World;
import org.cyclops.evilcraft.core.entity.item.EntityItemExtended;

/**
 * Entity item for an item that should have a defined rotation that does not change.
 * @author rubensworks
 *
 */
public abstract class EntityItemDefinedRotation extends EntityItemExtended {

	public EntityItemDefinedRotation(EntityType<? extends EntityItemDefinedRotation> type, World world, ItemEntity original) {
        super(type, world, original);
    }

	public EntityItemDefinedRotation(EntityType<? extends EntityItemDefinedRotation> type, World world) {
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
