package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.client.particle.ParticleBloodBubble;
import org.cyclops.evilcraft.core.block.BlockTileGuiTank;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;
import org.cyclops.evilcraft.tileentity.TileBloodInfuser;

import java.util.Random;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class BlockBloodInfuser extends BlockTileGuiTank {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final BooleanProperty ON = BooleanProperty.create("on");

    public BlockBloodInfuser(Block.Properties properties) {
        super(properties, TileBloodInfuser::new);

        this.setDefaultState(this.stateContainer.getBaseState()
                .with(FACING, Direction.NORTH));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public int getDefaultCapacity() {
        return TileBloodInfuser.LIQUID_PER_SLOT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        ParticleBloodBubble.randomDisplayTick((WorkingTileEntity) worldIn.getTileEntity(pos), worldIn, pos,
                rand, BlockHelpers.getSafeBlockStateProperty(stateIn, FACING, Direction.NORTH));
        super.animateTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return TileHelpers.getSafeTile(world, pos, TileBloodInfuser.class)
                .map(tile -> tile.isVisuallyWorking() ? 4 : super.getLightValue(state, world, pos))
                .orElse(0);
    }

    /*
    @Override
    public Class<? extends Container> getContainer() {
        return ContainerBloodInfuser.class;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Class<? extends Screen> getGui() {
        return GuiBloodInfuser.class;
    }*/ // TODO
}
