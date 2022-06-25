package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritReanimator;
import org.cyclops.evilcraft.client.particle.ParticleBloodBubble;
import org.cyclops.evilcraft.core.block.BlockWithEntityGuiTank;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;

import javax.annotation.Nullable;

/**
 * A machine that can reanimate spirits inside BOEC's.
 * @author rubensworks
 *
 */
public class BlockSpiritReanimator extends BlockWithEntityGuiTank {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final BooleanProperty ON = BooleanProperty.create("on");

    public BlockSpiritReanimator(Block.Properties properties) {
        super(properties, BlockEntitySpiritReanimator::new);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ON, false));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_SPIRIT_REANIMATOR, level.isClientSide ? new BlockEntitySpiritReanimator.TickerClient<>() : new BlockEntitySpiritReanimator.TickerServer<BlockEntitySpiritReanimator, MutableDouble>());
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
        return BlockEntitySpiritReanimator.LIQUID_PER_SLOT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState blockState, Level world, BlockPos blockPos, RandomSource random) {
        ParticleBloodBubble.randomDisplayTick((BlockEntityWorking) world.getBlockEntity(blockPos), world, blockPos,
                random, BlockHelpers.getSafeBlockStateProperty(blockState, FACING, Direction.NORTH));
        super.animateTick(blockState, world, blockPos, random);
    }
}
