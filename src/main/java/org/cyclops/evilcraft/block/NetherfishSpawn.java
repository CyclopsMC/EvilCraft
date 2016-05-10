package org.cyclops.evilcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocks;
import org.cyclops.evilcraft.entity.monster.Netherfish;
import org.cyclops.evilcraft.entity.monster.NetherfishConfig;

import java.util.Random;

/**
 * A blockState that spawns a {@link Netherfish} when the blockState breaks.
 * @author rubensworks
 *
 */
public class NetherfishSpawn extends ConfigurableBlockWithInnerBlocks {

    @BlockProperty
    public static final PropertyInteger FAKEMETA = PropertyInteger.create("meta", 0, 2);
    
    private static NetherfishSpawn _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static NetherfishSpawn getInstance() {
        return _instance;
    }

    public NetherfishSpawn(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.clay);
        this.setHardness(0.0F);
    }
    
    @Override
    protected IBlockState[] makeInnerBlockList() {
        return new IBlockState[]{
                Blocks.netherrack.getDefaultState(),
                Blocks.nether_brick.getDefaultState(),
                Blocks.soul_sand.getDefaultState()
        };
    }

    @Override
    protected PropertyInteger getMetaProperty() {
        return FAKEMETA;
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
