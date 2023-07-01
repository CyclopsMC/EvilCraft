package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableInt;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodInfuser;
import org.cyclops.evilcraft.client.particle.ParticleBloodBubble;
import org.cyclops.evilcraft.core.block.BlockWithEntityGuiTank;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;

import javax.annotation.Nullable;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class BlockBloodInfuser extends BlockWithEntityGuiTank {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final BooleanProperty ON = BooleanProperty.create("on");

    public BlockBloodInfuser(Block.Properties properties) {
        super(properties, BlockEntityBloodInfuser::new);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ON, false));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_BLOOD_INFUSER, level.isClientSide ? new BlockEntityBloodInfuser.TickerClient<>() : new BlockEntityBloodInfuser.TickerServer<BlockEntityBloodInfuser, MutableInt>());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ON);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(ON, false);
    }

    @Override
    public int getDefaultCapacity() {
        return BlockEntityBloodInfuser.LIQUID_PER_SLOT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        ParticleBloodBubble.randomDisplayTick((BlockEntityWorking) worldIn.getBlockEntity(pos), worldIn, pos,
                rand, BlockHelpers.getSafeBlockStateProperty(stateIn, FACING, Direction.NORTH));
        super.animateTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return BlockEntityHelpers.get(world, pos, BlockEntityBloodInfuser.class)
                .map(tile -> tile.isVisuallyWorking() ? 4 : super.getLightEmission(state, world, pos))
                .orElse(0);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        BlockEntityHelpers.get(world, pos, BlockEntityBloodInfuser.class)
                .ifPresent(tile -> {
                    EntityHelpers.spawnXpAtPlayer(player.level(), player, (int) Math.floor(tile.getXp()));
                    tile.resetXp();
                });
        return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
    }
}
