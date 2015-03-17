package evilcraft.core.block.component;

import evilcraft.client.particle.ExtendedEntityDropParticleFX;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.Random;

/**
 * Component that can show drops of a certain color underneath blocks.
 * This has by default the same behaviour as lava and water drops underneath the blocks underneath them.
 * But this offset can be altered.
 * @author rubensworks
 *
 */
public class EntityDropParticleFXBlockComponent implements IEntityDropParticleFXBlock{
    
    protected float particleRed;
    protected float particleGreen;
    protected float particleBlue;
    
    protected int offset = 1;
    protected int chance = 10;
    
    /**
     * Make a new instance.
     * @param particleRed Red color.
     * @param particleGreen Green color.
     * @param particleBlue Blue color.
     */
    public EntityDropParticleFXBlockComponent(float particleRed, float particleGreen, float particleBlue) {
        this.particleRed = particleRed;
        this.particleGreen = particleGreen;
        this.particleBlue = particleBlue;
    }
    
    /**
     * Set the offset to a lower Y where the drops should appear.
     * @param offset Amount of blocks to a lower Y.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    /**
     * Sets the chance for drop particles.
     * @param chance Every tick there will be a 1/chance chance.
     */
    public void setChance(int chance) {
        this.chance = chance;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState blockState, Random rand) {
        if (rand.nextInt(chance) == 0 &&
                (offset == 0 || World.doesBlockHaveSolidTopSurface(world, blockPos.add(0, - offset, 0))) &&
                !world.getBlockState(blockPos.add(0, - offset - 1, 0)).getBlock().getMaterial().blocksMovement()) {
            double px = (double) ((float) blockPos.getX() + rand.nextFloat());
            double py = (double) blockPos.getY() - 0.05D - offset;
            double pz = (double) ((float) blockPos.getZ() + rand.nextFloat());

            EntityFX fx = new ExtendedEntityDropParticleFX(world, px, py, pz, particleRed, particleGreen, particleBlue);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
        }
    }
    
}
