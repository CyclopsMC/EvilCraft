package org.cyclops.evilcraft.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.cyclops.cyclopscore.block.multi.CubeDetector;
import org.cyclops.cyclopscore.block.multi.DetectionResult;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityColossalBloodChest;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritFurnace;
import org.cyclops.evilcraft.core.block.BlockWithEntityGuiTank;

import javax.annotation.Nullable;

/**
 * A machine that can infuse stuff with blood.
 *
 * @author rubensworks
 */
public class BlockColossalBloodChest extends BlockWithEntityGuiTank implements CubeDetector.IDetectionListener {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockColossalBloodChest(Block.Properties properties) {
        super(properties, BlockEntityColossalBloodChest::new);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVE, false));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_COLOSSAL_BLOOD_CHEST, level.isClientSide ? new BlockEntityColossalBloodChest.TickerClient() : new BlockEntityColossalBloodChest.TickerServer());
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
    public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if(BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false)) {
            if (!BlockEntityColossalBloodChest.canWork(world, blockPos)) {
                return InteractionResult.FAIL;
            } else {
                return super.use(blockState, world, blockPos, player, hand, rayTraceResult);
            }
        } else {
            addPlayerChatError(world, blockPos, player, hand);
            return InteractionResult.FAIL;
        }
    }

    @Override
    public int getDefaultCapacity() {
        return BlockEntitySpiritFurnace.LIQUID_PER_SLOT;
    }

    public static void triggerDetector(LevelReader world, BlockPos blockPos, boolean valid) {
        BlockEntityColossalBloodChest.getCubeDetector().detect(world, blockPos, valid ? null : blockPos, true);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if (stack.hasCustomHoverName()) {
            BlockEntityColossalBloodChest tile = BlockEntityHelpers.get(world, pos, BlockEntityColossalBloodChest.class).orElse(null);
            if (tile != null) {
                tile.setSize(Vec3i.ZERO);
            }
        }
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
    public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState) {
        if(blockState.getValue(ACTIVE)) triggerDetector(world, blockPos, false);
        super.destroy(world, blockPos, blockState);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        if(BlockHelpers.getSafeBlockStateProperty(state, ACTIVE, false)) triggerDetector(world, pos, false);
        // IForgeBlock.super.onBlockExploded(state, world, pos, explosion);
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        wasExploded(world, pos, explosion);
    }

    @Override
    public void onDetect(LevelReader world, BlockPos location, Vec3i size, boolean valid, BlockPos originCorner) {
        Block block = world.getBlockState(location).getBlock();
        if(block == this) {
            ((Level) world).setBlock(location, world.getBlockState(location).setValue(ACTIVE, valid), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            BlockEntityHelpers.get(world, location, BlockEntityColossalBloodChest.class)
                    .ifPresent(tile -> {
                        tile.setSize(valid ? size : Vec3i.ZERO);
                        tile.setCenter(originCorner.offset(1, 1, 1));
                    });
        }
    }

    /**
     * Show the structure forming error in the given player chat window.
     * @param world The world.
     * @param blockPos The start position.
     * @param player The player.
     * @param hand The used hand.
     */
    public static void addPlayerChatError(Level world, BlockPos blockPos, Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND && !world.isClientSide && player.getItemInHand(hand).isEmpty()) {
            DetectionResult result = BlockEntityColossalBloodChest.getCubeDetector().detect(world, blockPos, null, false);
            if (result != null && result.getError() != null) {
                addPlayerChatError(player, result.getError());
            } else {
                player.sendSystemMessage(Component.translatable("multiblock.evilcraft.colossalbloodchest.error.unexpected"));
            }
        }
    }

    public static void addPlayerChatError(Player player, Component error) {
        MutableComponent chat = Component.literal("");
        MutableComponent prefix = Component.literal("[")
                .append(Component.translatable("multiblock.evilcraft.colossalbloodchest.error.prefix"))
                .append(Component.literal("]: "))
                .setStyle(Style.EMPTY.
                        applyFormat(ChatFormatting.GRAY).
                        withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Component.translatable("multiblock.evilcraft.colossalbloodchest.error.prefix.info")
                        ))
                );
        chat.append(prefix);
        chat.append(error);
        player.sendSystemMessage(chat);
    }

}
