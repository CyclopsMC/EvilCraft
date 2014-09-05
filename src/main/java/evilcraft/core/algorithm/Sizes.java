package evilcraft.core.algorithm;

import net.minecraft.entity.Entity;

/**
 * Helpers for {@link Size}.
 * @author rubensworks
 *
 */
public class Sizes {

	/**
	 * Get the size of an entity.
	 * @param entity The entity.
	 * @return The size.
	 */
	public static Size getEntitySize(Entity entity) {
		int x = ((int) Math.ceil(entity.width));
		int y = ((int) Math.ceil(entity.height));
		int z = x;
		return new Size(x, y, z);
	}
	
}
