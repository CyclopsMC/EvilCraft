package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.cyclops.cyclopscore.block.multi.CubeDetector;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.blockentity.BlockEntityColossalBloodChest;
import org.cyclops.evilcraft.core.algorithm.Wrapper;

import javax.annotation.Nullable;

/**
 * Part of the Colossal Blood Chest multiblock structure.
 * @author rubensworks
 *
 */
public class BlockReinforcedUndeadPlank extends Block implements CubeDetector.IDetectionListener {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockReinforcedUndeadPlank(Block.Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVE, false));
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
    public RenderShape getRenderShape(BlockState blockState) {
        return BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false) ? RenderShape.ENTITYBLOCK_ANIMATED : super.getRenderShape(blockState);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockReader, BlockPos blockPos) {
        return BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false);
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter world, BlockPos pos,
                                    SpawnPlacements.Type type, @Nullable EntityType<?> entityType) {
        return false;
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState blockState, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
        return true;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if(!world.captureBlockSnapshots) {
            BlockColossalBloodChest.triggerDetector(world, pos, true);
        }
    }

    @Override
    public void onPlace(BlockState blockStateNew, Level world, BlockPos blockPos, BlockState blockStateOld, boolean isMoving) {
        super.onPlace(blockStateNew, world, blockPos, blockStateOld, isMoving);
        if(!world.captureBlockSnapshots && blockStateNew.getBlock() != blockStateOld.getBlock() && !blockStateNew.getValue(ACTIVE)) {
            BlockColossalBloodChest.triggerDetector(world, blockPos, true);
        }
    }

    @Override
    public void destroy(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        super.destroy(worldIn, pos, state);
        if(BlockHelpers.getSafeBlockStateProperty(state, ACTIVE, false)) BlockColossalBloodChest.triggerDetector(worldIn, pos, false);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        if(BlockHelpers.getSafeBlockStateProperty(state, ACTIVE, false)) BlockColossalBloodChest.triggerDetector(world, pos, false);
        // IForgeBlock.super.onBlockExploded(state, world, pos, explosion);
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        wasExploded(world, pos, explosion);
    }

    @Override
    public void onDetect(LevelReader world, BlockPos location, Vec3i size, boolean valid, BlockPos originCorner) {
        Block block = world.getBlockState(location).getBlock();
        if(block == this) {
            boolean change = !BlockHelpers.getSafeBlockStateProperty(world.getBlockState(location), ACTIVE, false);
            ((Level) world).setBlock(location, world.getBlockState(location).setValue(ACTIVE, valid), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            if(change) {
                BlockEntityColossalBloodChest.detectStructure((Level) world, location, size, valid, originCorner);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult p_225533_6_) {
        if(BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false)) {
            final Wrapper<BlockPos> tileLocationWrapper = new Wrapper<BlockPos>();
            BlockEntityColossalBloodChest.getCubeDetector().detect(world, blockPos, null, new CubeDetector.IValidationAction() {

                @Override
                public Component onValidate(BlockPos location, BlockState blockState) {
                    if(blockState.getBlock() instanceof BlockColossalBloodChest) {
                        tileLocationWrapper.set(location);
                    }
                    return null;
                }

            }, false);
            BlockPos tileLocation = tileLocationWrapper.get();
            if(tileLocation != null) {
                return world.getBlockState(tileLocation).getBlock()
                        .use(world.getBlockState(tileLocation), world, tileLocation, player, hand, p_225533_6_);
            }
            return super.use(blockState, world, blockPos, player, hand, p_225533_6_);
        } else {
            BlockColossalBloodChest.addPlayerChatError(world, blockPos, player, hand);
            return InteractionResult.FAIL;
        }
    }

}
