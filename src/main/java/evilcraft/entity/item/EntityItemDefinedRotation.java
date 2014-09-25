package evilcraft.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import evilcraft.core.entity.item.EntityItemExtended;

/**
 * Entity item for an item that should have a defined rotation that does not change.
 * @author rubensworks
 *
 */
public abstract class EntityItemDefinedRotation extends EntityItemExtended {
	
	/**
     * New instance.
     * @param world The world.
     * @param original The original entity item/
     */
	public EntityItemDefinedRotation(World world, EntityItem original) {
        super(world, original);
    }
	
	protected abstract float getRotationYaw(World worldObj, double posX, double posY,
			double posZ);
	
	protected boolean hasCustomRotation() {
		return true;
	}
	
}
