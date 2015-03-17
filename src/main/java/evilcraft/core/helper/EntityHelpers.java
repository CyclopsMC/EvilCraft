package evilcraft.core.helper;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import java.util.List;

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
	 * correct method in {@link Block#onEntityCollidedWithBlock(World, BlockPos, Entity)}.
	 * @param world The world
	 * @param blockPos The position.
	 * @param entity The entity that collides.
	 */
	public static void onEntityCollided(World world, BlockPos blockPos, Entity entity) {
	    Block block = world.getBlockState(blockPos).getBlock();
	    if(block != null)
	        block.onEntityCollidedWithBlock(world, blockPos, entity);
	}

	/**
	 * Get the list of entities within a certain area.
	 * @param world The world to look in.
	 * @param blockPos The position.
	 * @param area The radius of the area.
	 * @return The list of entities in that area.
	 */
	public static List<Entity> getEntitiesInArea(World world, BlockPos blockPos, int area) {
	    AxisAlignedBB box = AxisAlignedBB.fromBounds(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                blockPos.getX(), blockPos.getY(), blockPos.getZ()).expand(area, area, area);
	    @SuppressWarnings("unchecked")
	    List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, box);
	    return entities;
	}
	
	/**
	 * Spawn the entity in the world.
	 * @param world The world.
	 * @param entityLiving The entity to spawn.
	 * @return If the entity was spawned.
	 */
	public static boolean spawnEntity(World world, EntityLiving entityLiving) {
		Result canSpawn = ForgeEventFactory.canEntitySpawn(entityLiving, world, (float) entityLiving.posX,
				(float) entityLiving.posY, (float) entityLiving.posZ);
        if (canSpawn == Result.ALLOW || (canSpawn == Result.DEFAULT)) { //  && entityliving.getCanSpawnHere()
            if (!ForgeEventFactory.doSpecialSpawn(entityLiving, world, (float) entityLiving.posX,
            		(float) entityLiving.posY, (float) entityLiving.posZ)) {
            	world.spawnEntityInWorld(entityLiving);
                return true;
            }
        }
        return false;
	}

    /**
     * Get the size of an entity.
     * @param entity The entity.
     * @return The size.
     */
    public static Vec3i getEntitySize(Entity entity) {
        int x = ((int) Math.ceil(entity.width));
        int y = ((int) Math.ceil(entity.height));
        int z = x;
        return new Vec3i(x, y, z);
    }
}
