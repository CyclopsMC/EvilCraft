package org.cyclops.evilcraft.core.config.configurable;

import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * {@link BasePressurePlateBlock} that can hold ExtendedConfigs.
 * @author rubensworks
 *
 */
public abstract class BlockPressurePlate extends BasePressurePlateBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockPressurePlate(Block.Properties properties) {
        super(properties, BlockSetType.IRON);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

}
