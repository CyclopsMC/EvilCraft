package org.cyclops.evilcraft.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.World;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.entity.item.EntityItemIndestructable;
import org.cyclops.evilcraft.item.IItemEmpowerable;

/**
 * Entity item for an {@link IItemEmpowerable}.
 * @author rubensworks
 *
 */
public class EntityItemEmpowerable extends EntityItemIndestructable {

	public EntityItemEmpowerable(EntityType<? extends EntityItemEmpowerable> type, World world) {
		super(type, world);
	}

	public EntityItemEmpowerable(World world, ItemEntity original) {
		super(RegistryEntries.ENTITY_ITEM_EMPOWERABLE, world, original);
	}
	
	@Override
	protected boolean isIndestructable() {
        if (!(getItem().getItem() instanceof IItemEmpowerable)) {
        	return super.isIndestructable();
		}
		return ((IItemEmpowerable) getItem().getItem()).isEmpowered(getItem());
	}
	
}
