package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import org.cyclops.evilcraft.core.block.BlockTileGuiTank;
import org.cyclops.evilcraft.tileentity.TileBloodChest;

/**
 * A chest that runs on blood and repairs tools.
 * @author rubensworks
 *
 */
public class BlockBloodChest extends BlockTileGuiTank {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public BlockBloodChest(Block.Properties properties) {
        super(properties, TileBloodChest::new);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public int getDefaultCapacity() {
        return TileBloodChest.LIQUID_PER_SLOT;
    }

    /*
    @Override
    public Class<? extends Container> getContainer() {
        return ContainerBloodChest.class;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Class<? extends Screen> getGui() {
        return GuiBloodChest.class;
    }*/ // TODO
}
