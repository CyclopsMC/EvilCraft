package evilcraft.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import evilcraft.core.entity.item.EntityItemIndestructable;
import evilcraft.item.IItemEmpowerable;

/**
 * Entity item for an {@link IItemEmpowerable}.
 * @author rubensworks
 *
 */
public class EntityItemEmpowerable extends EntityItemIndestructable {

	/**
     * New instance.
     * @param world The world.
     * @param original The original item entity
     */
	public EntityItemEmpowerable(World world, EntityItem original) {
        super(world, original);
    }
	
	@Override
	protected boolean isIndestructable() {
		return ((IItemEmpowerable) getEntityItem().getItem()).isEmpowered(getEntityItem());
	}
	
}
