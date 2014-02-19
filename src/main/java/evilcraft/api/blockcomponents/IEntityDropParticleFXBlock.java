package evilcraft.api.blockcomponents;

import java.util.Random;

import net.minecraft.world.World;

/**
 * Interface for the {@link EntityDropParticleFXBlockComponent}.
 * @author rubensworks
 *
 */
public interface IEntityDropParticleFXBlock {
    /**
     * Called when a random display tick occurs.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param rand Random object.
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random rand);
}
