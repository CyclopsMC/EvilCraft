package org.cyclops.evilcraft.core.config.configurable;

import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;

/**
 * {@link AbstractPressurePlateBlock} that can hold ExtendedConfigs.
 * @author rubensworks
 *
 */
public abstract class BlockPressurePlate extends AbstractPressurePlateBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockPressurePlate(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

}
