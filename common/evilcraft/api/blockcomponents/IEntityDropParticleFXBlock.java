package evilcraft.api.blockcomponents;

import java.util.Random;

import net.minecraft.world.World;

/**
 * Interface for the EntityDropParticleFXBlockComponent.
 * @author rubensworks
 *
 */
public interface IEntityDropParticleFXBlock {
    public void randomDisplayTick(World world, int x, int y, int z, Random rand);
}
