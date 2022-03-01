package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityEnvironmentalAccumulator;

import javax.annotation.Nullable;

/**
 * Block that can collect the weather and stuff.
 * @author immortaleeb
 *
 */
public class BlockEnvironmentalAccumulator extends BlockWithEntity {

    /**
     * State indicating the environmental accumulator is idle.
     */
    public static final int STATE_IDLE = 0;
    /**
     * State indicating the environmental accumulator is currently
     * processing an item.
     */
    public static final int STATE_PROCESSING_ITEM = 1;
    /**
     * State indicating the environmental accumulator is cooling
     * down.
     */
    public static final int STATE_COOLING_DOWN = 2;
    /**
     * State indicating the environmental accumulator has just
     * finished processing an item. This state is necessary
     * because using this state we can put some delay between
     * processing an item and cooling down so that the client
     * gets a moment to show an effect when an item has finished
     * processing.
     */
    public static final int STATE_FINISHED_PROCESSING_ITEM = 3;

    public BlockEnvironmentalAccumulator(Block.Properties properties) {
        super(properties, BlockEntityEnvironmentalAccumulator::new);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_ENVIRONMENTAL_ACCUMULATOR, new BlockEntityEnvironmentalAccumulator.Ticker());
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return BlockEntityHelpers.get(worldIn, pos, BlockEntityEnvironmentalAccumulator.class)
                .map(tile -> tile.getState() == STATE_IDLE ? 15 : 0)
                .orElse(0);
    }
}
