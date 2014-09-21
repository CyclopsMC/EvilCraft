package evilcraft.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import evilcraft.core.entity.item.EntityItemIndestructable;

/**
 * Entity item that can not despawn.
 * @author rubensworks
 *
 */
public class EntityItemUndespawnable extends EntityItemIndestructable {

	/**
     * New instance.
     * @param world The world.
     * @param original The original item entity
     */
	public EntityItemUndespawnable(World world, EntityItem original) {
        super(world, original);
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
