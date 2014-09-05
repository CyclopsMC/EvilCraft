package evilcraft.core.helpers;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Helpers for entities.
 * @author rubensworks
 *
 */
public class EntityHelpers {

	/**
	 * The NBT tag name that is used for storing the unique name id for an entity.
	 */
	public static final String NBTTAG_ID = "id";

	/**
	 * This should by called when custom entities collide. It will call the
	 * correct method in {@link Block#onEntityCollidedWithBlock(World, int, int, int, Entity)}.
	 * @param world The world
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param entity The entity that collides.
	 */
	public static void onEntityCollided(World world, int x, int y, int z, Entity entity) {
	    Block block = world.getBlock(x, y, z);
	    if(block != null)
	        block.onEntityCollidedWithBlock(world, x, y, z, entity);
	}

	/**
	 * Get the list of entities within a certain area.
	 * @param world The world to look in.
	 * @param x The center X coordinate.
	 * @param y The center Y coordinate.
	 * @param z The center Z coordinate.
	 * @param area The radius of the area.
	 * @return The list of entities in that area.
	 */
	public static List<Entity> getEntitiesInArea(World world, int x, int y, int z, int area) {
	    AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(area, area, area);
	    @SuppressWarnings("unchecked")
	    List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, box);
	    return entities;
	}
	
}
