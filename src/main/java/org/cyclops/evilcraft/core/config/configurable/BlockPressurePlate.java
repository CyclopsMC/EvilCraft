package org.cyclops.evilcraft.core.config.configurable;

import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * {@link AbstractPressurePlateBlock} that can hold ExtendedConfigs.
 * @author rubensworks
 *
 */
public abstract class BlockPressurePlate extends AbstractPressurePlateBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockPressurePlate(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(POWERED, Boolean.FALSE));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if(!isValidPosition(state, worldIn, pos)) {
            worldIn.removeBlock(pos, false);
        }
    }

}
