package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.apache.commons.lang3.mutable.MutableInt;
import org.cyclops.cyclopscore.block.BlockWithEntityGui;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntitySanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;

import javax.annotation.Nullable;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class BlockSanguinaryEnvironmentalAccumulator extends BlockWithEntityGui {

    public static final BooleanProperty ON = BooleanProperty.create("on");

    public BlockSanguinaryEnvironmentalAccumulator(Block.Properties properties) {
        super(properties, BlockEntitySanguinaryEnvironmentalAccumulator::new);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ON, false));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR, level.isClientSide ? new BlockEntitySanguinaryEnvironmentalAccumulator.TickerClient() : new BlockEntitySanguinaryEnvironmentalAccumulator.TickerServer<BlockEntitySanguinaryEnvironmentalAccumulator, MutableInt>());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ON);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ON, false);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return BlockEntityHelpers.get(world, pos, BlockEntitySanguinaryEnvironmentalAccumulator.class)
                .filter(BlockEntityWorking::isVisuallyWorking)
                .map(tile -> 4)
                .orElseGet(() -> super.getLightEmission(state, world, pos));
    }

    @Override
    public void onRemove(BlockState oldState, Level world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide() && oldState.getBlock() != newState.getBlock()) {
            BlockEntityHelpers.get(world, blockPos, BlockEntitySanguinaryEnvironmentalAccumulator.class)
                    .ifPresent(tile -> InventoryHelpers.dropItems(world, tile.getInventory(), blockPos));
        }
        super.onRemove(oldState, world, blockPos, newState, isMoving);
    }

}
