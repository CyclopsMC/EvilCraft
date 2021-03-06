package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
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
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.client.particle.ParticleBloodBubble;
import org.cyclops.evilcraft.core.block.BlockTileGuiTank;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
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
                .with(FACING, Direction.NORTH)
                .with(ON, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, ON);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState()
                .with(FACING, context.getPlacementHorizontalFacing())
                .with(ON, false);
    }

    @Override
    public int getDefaultCapacity() {
        return TileBloodInfuser.LIQUID_PER_SLOT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        ParticleBloodBubble.randomDisplayTick((TileWorking) worldIn.getTileEntity(pos), worldIn, pos,
                rand, BlockHelpers.getSafeBlockStateProperty(stateIn, FACING, Direction.NORTH));
        super.animateTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return TileHelpers.getSafeTile(world, pos, TileBloodInfuser.class)
                .map(tile -> tile.isVisuallyWorking() ? 4 : super.getLightValue(state, world, pos))
                .orElse(0);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        TileHelpers.getSafeTile(world, pos, TileBloodInfuser.class)
                .ifPresent(tile -> {
                    EntityHelpers.spawnXpAtPlayer(player.world, player, (int) Math.floor(tile.getXp()));
                    tile.resetXp();
                });
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }
}
