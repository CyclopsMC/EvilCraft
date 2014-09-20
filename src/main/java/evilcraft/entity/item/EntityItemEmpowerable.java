package evilcraft.entity.item;

import net.minecraft.item.ItemStack;
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
     * @param x X
     * @param y Y
     * @param z Z
     * @param itemStack The item stack to set.
     */
	public EntityItemEmpowerable(World world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
    }
	
	@Override
	protected boolean isIndestructable() {
		return ((IItemEmpowerable) getEntityItem().getItem()).isEmpowered(getEntityItem());
	}
	
}
