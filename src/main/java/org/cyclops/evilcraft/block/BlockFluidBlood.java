package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.RegistryEntries;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * A blockState for the blood fluid.
 * @author rubensworks
 *
 */
public class BlockFluidBlood extends FlowingFluidBlock {
    
    private static final int CHANCE_HARDEN = 3;

    public BlockFluidBlood(Block.Properties builder) {
        super(() -> RegistryEntries.FLUID_BLOOD, builder);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(LEVEL) == 0;
    }

    @Override
    public void randomTick(BlockState blockState, ServerWorld world, BlockPos blockPos, Random random) {
        if(random.nextInt(CHANCE_HARDEN) == 0 && blockState.getValue(LEVEL) == 0
                && (!(world.isRaining() && world.getBiome(blockPos).getDownfall() > 0) || !world.canSeeSkyFromBelowWater(blockPos))
                && !isWaterInArea(world, blockPos)) {
            world.setBlock(blockPos, RegistryEntries.BLOCK_HARDENED_BLOOD.defaultBlockState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        }
        super.randomTick(blockState, world, blockPos, random);
    }

    protected boolean isWaterInArea(World world, BlockPos blockPos) {
        return WorldHelpers.foldArea(world, 4, blockPos,
                (input, world1, blockPos1) -> input || world1.getBlockState(blockPos1).getBlock() == Blocks.WATER, false);
    }

}
