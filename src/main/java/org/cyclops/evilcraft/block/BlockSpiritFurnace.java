package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.cyclops.cyclopscore.block.multi.CubeDetector;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritFurnace;
import org.cyclops.evilcraft.core.block.BlockWithEntityGuiTank;

import javax.annotation.Nullable;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class BlockSpiritFurnace extends BlockWithEntityGuiTank implements CubeDetector.IDetectionListener {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockSpiritFurnace(Block.Properties properties) {
        super(properties, BlockEntitySpiritFurnace::new);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVE, false));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_SPIRIT_FURNACE, level.isClientSide ? new BlockEntitySpiritFurnace.TickerClient<>() : new BlockEntitySpiritFurnace.TickerServer<BlockEntitySpiritFurnace, MutableDouble>());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ACTIVE, false);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (!BlockEntitySpiritFurnace.canWork(world, blockPos)) {
            return InteractionResult.FAIL;
        }
        return super.use(blockState, world, blockPos, player, hand, rayTraceResult);
    }

    private void triggerDetector(LevelAccessor world, BlockPos blockPos, boolean valid) {
        BlockEntitySpiritFurnace.getCubeDetector().detect(world, blockPos, valid ? null : blockPos, true);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        triggerDetector(world, pos, true);
    }

    @Override
    public void onPlace(BlockState blockStateNew, Level world, BlockPos blockPos, BlockState blockStateOld, boolean isMoving) {
        super.onPlace(blockStateNew, world, blockPos, blockStateOld, isMoving);
        if(!world.captureBlockSnapshots && blockStateNew.getBlock() != blockStateOld.getBlock() && !blockStateNew.getValue(ACTIVE)) {
            triggerDetector(world, blockPos, true);
        }
    }

    @Override
    public void destroy(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        super.destroy(worldIn, pos, state);
        if(state.getValue(ACTIVE)) triggerDetector(worldIn, pos, false);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        if(world.getBlockState(pos).getValue(ACTIVE)) triggerDetector(world, pos, false);
        // IForgeBlock.super.onBlockExploded(state, world, pos, explosion);
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        this.wasExploded(world, pos, explosion);
    }

    @Override
    public void onDetect(LevelReader world, BlockPos location, Vec3i size, boolean valid, BlockPos originCorner) {
        Block block = world.getBlockState(location).getBlock();
        if(block == this) {
            boolean change = !world.getBlockState(location).getValue(ACTIVE);
            ((Level) world).setBlock(location, world.getBlockState(location).setValue(ACTIVE, valid), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            BlockEntity tile = world.getBlockEntity(location);
            if(tile != null) {
                ((BlockEntitySpiritFurnace) tile).setSize(valid ? size : Vec3i.ZERO);
            }
            if(change) {
                BlockEntitySpiritFurnace.detectStructure(world, location, size, valid, originCorner);
            }
        }
    }

    @Override
    public int getDefaultCapacity() {
        return BlockEntitySpiritFurnace.LIQUID_PER_SLOT;
    }
}
