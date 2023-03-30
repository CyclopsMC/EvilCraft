package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * A blockState for the blood fluid.
 * @author rubensworks
 *
 */
public class BlockFluidBlood extends LiquidBlock {

    private static final int CHANCE_HARDEN = 3;

    public BlockFluidBlood(Block.Properties builder) {
        super(() -> RegistryEntries.FLUID_BLOOD, builder);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(LEVEL) == 0;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        if(random.nextInt(CHANCE_HARDEN) == 0 && blockState.getValue(LEVEL) == 0
                && (!(world.isRaining() && world.getBiome(blockPos).value().hasPrecipitation()) || !world.canSeeSkyFromBelowWater(blockPos))
                && !isWaterInArea(world, blockPos)) {
            world.setBlock(blockPos, RegistryEntries.BLOCK_HARDENED_BLOOD.defaultBlockState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        }
        super.randomTick(blockState, world, blockPos, random);
    }

    protected boolean isWaterInArea(Level world, BlockPos blockPos) {
        return WorldHelpers.foldArea(world, 4, blockPos,
                (input, world1, blockPos1) -> input || world1.getBlockState(blockPos1).getBlock() == Blocks.WATER, false);
    }

}
