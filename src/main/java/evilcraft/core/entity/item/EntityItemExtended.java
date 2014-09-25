package evilcraft.core.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;

/**
 * An extended version of the entity item.
 * @author rubensworks
 *
 */
public abstract class EntityItemExtended extends EntityItem {
    
    /**
     * New instance.
     * @param world The world.
     * @param original The original entity item.
     */
	public EntityItemExtended(World world, EntityItem original) {
        super(world, original.posX, original.posY, original.posZ, original.getEntityItem());
        delayBeforeCanPickup = 40;
        motionX = original.motionX;
        motionY = original.motionY;
        motionZ = original.motionZ;
    }
	
}
