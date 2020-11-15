package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import org.cyclops.cyclopscore.block.BlockTileGui;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class BlockSanguinaryEnvironmentalAccumulator extends BlockTileGui {

    public static final BooleanProperty ON = BooleanProperty.create("on");

    public BlockSanguinaryEnvironmentalAccumulator(Block.Properties properties) {
        super(properties, TileSanguinaryEnvironmentalAccumulator::new);

        this.setDefaultState(this.stateContainer.getBaseState()
                .with(ON, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ON);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(ON, false);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return TileHelpers.getSafeTile(world, pos, TileSanguinaryEnvironmentalAccumulator.class)
                .filter(WorkingTileEntity::isVisuallyWorking)
                .map(tile -> 4)
                .orElseGet(() -> super.getLightValue(state, world, pos));
    }

}
