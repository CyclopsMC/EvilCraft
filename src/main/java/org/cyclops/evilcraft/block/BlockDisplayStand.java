package org.cyclops.evilcraft.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.model.data.ModelProperty;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDisplayStand;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * A stand for displaying items.
 * @author rubensworks
 *
 */
public class BlockDisplayStand extends BlockWithEntity {

    public static final MapCodec<BlockDisplayStand> CODEC = simpleCodec(BlockDisplayStand::new);

    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class, Direction.values());
    public static final BooleanProperty AXIS_X = BooleanProperty.create("axis_x");

    // Model Properties
    public static final ModelProperty<Direction.AxisDirection> DIRECTION = new ModelProperty<>();
    public static final ModelProperty<ItemStack> TYPE = new ModelProperty<>();

    public static final Map<Direction, VoxelShape> FACING_BOUNDS = ImmutableMap.<Direction, VoxelShape>builder()
            .put(Direction.DOWN, Block.box(0.375F * 16F, 0.0F, 0.375F * 16F, 0.625F * 16F, 0.5F * 16F, 0.625F * 16F))
            .put(Direction.UP, Block.box(0.375F * 16F, 0.5F * 16F, 0.375F * 16F, 0.625F * 16F, 1.0F * 16F, 0.625F * 16F))
            .put(Direction.WEST, Block.box(0.0F, 0.375F * 16F, 0.375F * 16F, 0.5F * 16F, 0.625F * 16F, 0.625F * 16F))
            .put(Direction.EAST, Block.box(0.5F * 16F, 0.375F * 16F, 0.375F * 16F, 1.0F * 16F, 0.625F * 16F, 0.625F * 16F))
            .put(Direction.NORTH, Block.box(0.375F * 16F, 0.375F * 16F, 0.0F, 0.625F * 16F, 0.625F * 16F, 0.5F * 16F))
            .put(Direction.SOUTH, Block.box(0.375F * 16F, 0.375F * 16F, 0.5F * 16F, 0.625F * 16F, 0.625F * 16F, 1.0F * 16F))
            .build();

    public BlockDisplayStand(Block.Properties properties) {
        super(properties, BlockEntityDisplayStand::new);
        NeoForge.EVENT_BUS.register(this);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(AXIS_X, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_DISPLAY_STAND.get(), new BlockEntityTickerDelayed<>());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AXIS_X);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return FACING_BOUNDS.get(IModHelpers.get().getBlockHelpers().getSafeBlockStateProperty(state, FACING, Direction.DOWN));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos blockPos, Direction direction) {
        return IModHelpers.get().getBlockEntityHelpers().get(world, blockPos, BlockEntityDisplayStand.class)
                .map(tile -> !tile.getInventory().getItem(0).isEmpty() ? 15 : 0)
                .orElse(0);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = super.getStateForPlacement(context);
        blockState = blockState.setValue(FACING, context.getClickedFace().getOpposite());
        Direction playerFacing = context.getPlayer().getDirection();
        boolean axisX;
        if (context.getClickedFace().getOpposite() == Direction.DOWN || context.getClickedFace().getOpposite() == Direction.UP) {
            axisX = playerFacing.getAxis() == Direction.Axis.X;
        } else {
            axisX = playerFacing.getAxis() != Direction.Axis.X && playerFacing.getAxis() != Direction.Axis.Z;
        }
        blockState = blockState.setValue(AXIS_X, axisX);
        return blockState;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(world, blockPos, blockState, entity, stack);
        if (!world.isClientSide()) {
            IModHelpers.get().getBlockEntityHelpers().get(world, blockPos, BlockEntityDisplayStand.class)
                    .ifPresent(tile -> {
                        tile.setDisplayStandType(getDisplayStandType(stack));
                        tile.setDirection(entity.getDirection().getAxisDirection());
                    });
        }
    }

    @Override
    public BlockState rotate(BlockState blockState, LevelAccessor world, BlockPos pos, Rotation direction) {
        return IModHelpers.get().getBlockEntityHelpers().get(world, pos, BlockEntityDisplayStand.class)
                .map(tile -> {
                    if (tile.getDirection() == Direction.AxisDirection.POSITIVE) {
                        if (blockState.getValue(AXIS_X)) {
                            tile.setDirection(Direction.AxisDirection.POSITIVE);
                            return blockState.setValue(AXIS_X, false);
                        } else {
                            tile.setDirection(Direction.AxisDirection.NEGATIVE);
                            return blockState.setValue(AXIS_X, true);
                        }
                    } else {
                        if (blockState.getValue(AXIS_X)) {
                            tile.setDirection(Direction.AxisDirection.NEGATIVE);
                            return blockState.setValue(AXIS_X, false);
                        } else {
                            tile.setDirection(Direction.AxisDirection.POSITIVE);
                            return blockState.setValue(AXIS_X, true);
                        }
                    }
                })
                .orElse(blockState);
    }

    public void fillItemCategory(NonNullList<ItemStack> list) {
        try {
            BuiltInRegistries.ITEM.get(ItemTags.PLANKS)
                    .ifPresent(values -> values.forEach(holder -> {
                        Item item = holder.value();
                        if (item instanceof BlockItem) {
                            BlockState plankWoodBlockState = IModHelpers.get().getBlockHelpers().getBlockStateFromItemStack(new ItemStack(item));
                            list.add(getTypedDisplayStandItem(plankWoodBlockState));
                        }
                    }));
        } catch (IllegalStateException e) {
            // Can occur during mod loading when the tag has not been set yet
        }
    }

    public ItemStack getTypedDisplayStandItem(BlockState blockState) {
        ItemStack itemStack = new ItemStack(this);
        itemStack.set(RegistryEntries.COMPONENT_DISPLAY_STAND_TYPE, blockState);
        return itemStack;
    }

    public ItemStack getDisplayStandType(ItemStack displayStandStack) {
        BlockState blockState = displayStandStack.get(RegistryEntries.COMPONENT_DISPLAY_STAND_TYPE);
        if (blockState != null) {
            return IModHelpers.get().getBlockHelpers().getItemStackFromBlockState(blockState);
        }
        return null;
    }

    public static void setDisplayStandType(ItemStack displayStandStack, ItemStack type) {
        displayStandStack.set(RegistryEntries.COMPONENT_DISPLAY_STAND_TYPE, IModHelpers.get().getBlockHelpers().getBlockStateFromItemStack(type));
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        // Force allow right clicking with a fluid container passing through to this block
        if (!event.getItemStack().isEmpty()
                && event.getItemStack().getCapability(Capabilities.Fluid.ITEM, ItemAccess.forPlayerInteraction(event.getEntity(), event.getHand())) != null
                && event.getLevel().getBlockState(event.getPos()).getBlock() == this) {
            event.setUseBlock(TriState.TRUE);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult pHitResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntityDisplayStand tile = IModHelpers.get().getBlockEntityHelpers().get(world, pos, BlockEntityDisplayStand.class).orElse(null);
            if (tile != null) {
                ItemStack tileStack = tile.getInventory().getItem(0);
                if ((itemStack.isEmpty() || (ItemStack.isSameItemSameComponents(itemStack, tileStack) && tileStack.getCount() < tileStack.getMaxStackSize())) && !tileStack.isEmpty()) {
                    if(!itemStack.isEmpty()) {
                        tileStack.grow(itemStack.getCount());
                    }
                    player.getInventory().setSelectedItem(tileStack);
                    tile.getInventory().setItem(0, ItemStack.EMPTY);
                    tile.sendUpdate();
                    return InteractionResult.SUCCESS;
                } else if (!itemStack.isEmpty() && tile.getInventory().getItem(0).isEmpty()) {
                    tile.getInventory().setItem(0, itemStack.split(1));
                    if (itemStack.getCount() <= 0)
                        player.getInventory().setSelectedItem(ItemStack.EMPTY);
                    tile.sendUpdate();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(pStack, pState, world, pos, player, hand, pHitResult);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return IModHelpers.get().getBlockHelpers().doesBlockHaveSolidTopSurface(worldIn, pos);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        if (!worldIn.isAreaLoaded(pos, 1))
            return;
        if (!state.canSurvive(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (level instanceof ServerLevel serverLevel && !state.canSurvive(level, pos)) {
            serverLevel.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }
}
