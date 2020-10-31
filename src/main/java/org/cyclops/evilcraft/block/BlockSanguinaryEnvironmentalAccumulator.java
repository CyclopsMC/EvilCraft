package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
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
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return TileHelpers.getSafeTile(world, pos, TileSanguinaryEnvironmentalAccumulator.class)
                .filter(WorkingTileEntity::isVisuallyWorking)
                .map(tile -> 4)
                .orElseGet(() -> super.getLightValue(state, world, pos));
    }

    /*
    @Override
    public Class<? extends Container> getContainer() {
        return ContainerSanguinaryEnvironmentalAccumulator.class;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Class<? extends Screen> getGui() {
        return GuiSanguinaryEnvironmentalAccumulator.class;
    }
    TODO: gui
     */
}
