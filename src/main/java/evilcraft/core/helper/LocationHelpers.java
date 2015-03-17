package evilcraft.core.helper;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import java.util.Random;

/**
 * Helper methods involving {@link BlockPos}S and {@link TargetPoint}S.
 * @author immortaleeb
 *
 */
public class LocationHelpers {
	
	private static final Random random = new Random();

	/**
	 * Creates a {@link TargetPoint} for the dimension and position of the given {@link Entity}
	 * and a given range.
	 * 
	 * @param entity Entity who's dimension and position will be used to create the {@link TargetPoint}. 
	 * @param range The range of the {@link TargetPoint}.
	 * @return A {@link TargetPoint} with the position and dimension of the entity and the given range.
	 */
	public static TargetPoint createTargetPointFromEntityPosition(Entity entity, int range) {
		return new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, range);
	}

	/**
	 * Creates a {@link TargetPoint} for the dimension of the given world and the
	 * given {@link BlockPos}.
	 * 
	 * @param world The world from which the dimension will be used.
	 * @param location The location for the target.
	 * @param range The range of the {@link TargetPoint}.
	 * @return A {@link TargetPoint} with the position and dimension of the entity and the given range.
	 */
	public static TargetPoint createTargetPointFromLocation(World world, BlockPos location,
			int range) {
		return new TargetPoint(world.provider.getDimensionId(), location.getX(), location.getY(), location.getZ(), range);
	}
	
	/**
	 * Get a random point inside a sphere in an efficient way.
	 * @param center The center coordinates of the sphere.
	 * @param radius The radius of the sphere.
	 * @return The coordinates of the random point.
	 */
	public static BlockPos getRandomPointInSphere(BlockPos center, int radius) {
		BlockPos randomPoint = null;
	    while(randomPoint == null) {
            BlockPos coordinates = center.add(- radius + random.nextInt(2 * radius),
                    - radius + random.nextInt(2 * radius), - radius + random.nextInt(2 * radius));
            double totalDistance = center.distanceSq(coordinates);
	    	if((int) Math.sqrt(totalDistance) <= radius) {
	    		randomPoint = coordinates;
	    	}
	    }
	    return randomPoint;
	}

    public static BlockPos copyLocation(BlockPos blockPos) {
        return new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static Vec3i copyLocation(Vec3i blockPos) {
        return new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static BlockPos addToDimension(BlockPos blockPos, int dimension, int value) {
        if(dimension == 0) return blockPos.add(value, 0, 0);
        if(dimension == 1) return blockPos.add(0, value, 0);
        if(dimension == 2) return blockPos.add(0, 0, value);
        return blockPos;
    }

    public static BlockPos fromArray(int[] coordinates) {
        return new BlockPos(coordinates[0], coordinates[1], coordinates[2]);
    }

    public static int[] toArray(Vec3i blockPos) {
        return new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()};
    }

    public static BlockPos subtract(BlockPos blockPos, Vec3i vec) {
        return new BlockPos(blockPos.getX() - vec.getX(), blockPos.getY() - vec.getY(), blockPos.getZ() - vec.getZ());
    }

    public static Vec3i subtract(Vec3i vec1, Vec3i vec2) {
        return new Vec3i(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY(), vec1.getZ() - vec2.getZ());
    }

}
