package org.cyclops.evilcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockFluidClassic;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import org.cyclops.evilcraft.fluid.Blood;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * A blockState for the {@link Blood} fluid.
 * @author rubensworks
 *
 */
public class FluidBlockBlood extends ConfigurableBlockFluidClassic {
    
    private static final int CHANCE_HARDEN = 10;

    private static FluidBlockBlood _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static FluidBlockBlood getInstance() {
        return _instance;
    }

    public FluidBlockBlood(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Blood.getInstance(), Material.water);
        
        if (MinecraftHelpers.isClientSide())
            this.setParticleColor(1.0F, 0.0F, 0.0F);
        this.setTickRandomly(true);
    }
    
    @Override
    public int tickRate(World par1World) {
        return 100;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState blockState, Random random) {
        if(random.nextInt(CHANCE_HARDEN) == 0 &&
                isSourceBlock(world, blockPos) &&
                (!(world.isRaining() && ObfuscationHelpers.isRainingEnabled(world.getBiomeGenForCoords(blockPos)))
                        || !world.canBlockSeeSky(blockPos))
                && !isWaterInArea(world, blockPos)) {
            world.setBlockState(blockPos, HardenedBlood.getInstance().getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        } else {
            super.updateTick(world, blockPos, blockState, random);
        }
        world.scheduleUpdate(blockPos, this, tickRate(world));
    }

    protected boolean isWaterInArea(World world, BlockPos blockPos) {
        return WorldHelpers.foldArea(world, 4, blockPos, new WorldHelpers.WorldFoldingFunction<Boolean, Boolean>() {

            @Nullable
            @Override
            public Boolean apply(@Nullable Boolean input, World world, BlockPos blockPos) {
                return (input == null || input) || world.getBlockState(blockPos).getBlock() == Blocks.water;
            }

        }, false);
    }

}
