package evilcraft.block;

import evilcraft.Configs;
import evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocks;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.entity.monster.Netherfish;
import evilcraft.entity.monster.NetherfishConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * A blockState that spawns a {@link Netherfish} when the blockState breaks.
 * @author rubensworks
 *
 */
public class NetherfishSpawn extends ConfigurableBlockWithInnerBlocks {
    
    private static NetherfishSpawn _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new NetherfishSpawn(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static NetherfishSpawn getInstance() {
        return _instance;
    }

    private NetherfishSpawn(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.clay);
        this.setHardness(0.0F);
    }
    
    @Override
    protected Block[] makeInnerBlockList() {
        return new Block[]{
                Blocks.netherrack,
                Blocks.nether_brick, 
                Blocks.soul_sand
                };
    }
    
    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState blockState) {
        if (!world.isRemote && Configs.isEnabled(NetherfishConfig.class)) {
            Netherfish netherfish = new Netherfish(world);
            netherfish.setLocationAndAngles((double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, 0.0F, 0.0F);
            world.spawnEntityInWorld(netherfish);
            netherfish.spawnExplosionParticle();
        }

        super.onBlockDestroyedByPlayer(world, blockPos, blockState);
    }
    
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
    
    /**
     * Does the given metadata correspond to an inner blockState?
     * @param blockState The blockstate.
     * @return if the metadata corresponds to an inner blockState.
     */
    public boolean getPosingIdByMetadata(IBlockState blockState) {
        return getBlockFromState(blockState) != null;
    }

}
