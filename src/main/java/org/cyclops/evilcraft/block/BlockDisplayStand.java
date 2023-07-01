package org.cyclops.evilcraft.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDisplayStand;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * A stand for displaying items.
 * @author rubensworks
 *
 */
public class BlockDisplayStand extends BlockWithEntity {

    private static final String NBT_TYPE = "displayStandType";

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
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
        MinecraftForge.EVENT_BUS.register(this);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(AXIS_X, false));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_DISPLAY_STAND, new BlockEntityTickerDelayed<>());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AXIS_X);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return FACING_BOUNDS.get(BlockHelpers.getSafeBlockStateProperty(state, FACING, Direction.DOWN));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos blockPos) {
        return BlockEntityHelpers.get(world, blockPos, BlockEntityDisplayStand.class)
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
            BlockEntityHelpers.get(world, blockPos, BlockEntityDisplayStand.class)
                    .ifPresent(tile -> {
                        tile.setDisplayStandType(getDisplayStandType(stack));
                        tile.setDirection(entity.getDirection().getAxisDirection());
                    });
        }
    }

    @Override
    public BlockState rotate(BlockState blockState, LevelAccessor world, BlockPos pos, Rotation direction) {
        return BlockEntityHelpers.get(world, pos, BlockEntityDisplayStand.class)
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
            ForgeRegistries.ITEMS.tags().getTag(ItemTags.PLANKS).stream()
                    .forEach(item -> {
                        if (item instanceof BlockItem) {
                            BlockState plankWoodBlockState = BlockHelpers.getBlockStateFromItemStack(new ItemStack(item));
                            list.add(getTypedDisplayStandItem(plankWoodBlockState));
                        }
                    });
        } catch (IllegalStateException e) {
            // Can occur during mod loading when the tag has not been set yet
        }
    }

    public ItemStack getTypedDisplayStandItem(BlockState blockState) {
        Tag blockTag = BlockHelpers.serializeBlockState(blockState);
        ItemStack itemStack = new ItemStack(this);
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.put(NBT_TYPE, blockTag);
        return itemStack;
    }

    public ItemStack getDisplayStandType(ItemStack displayStandStack) {
        CompoundTag tag = displayStandStack.getTag();
        if (tag != null && tag.contains(NBT_TYPE)) {
            BlockState blockState = BlockHelpers.deserializeBlockState(BlockHelpers.HOLDER_GETTER_FORGE, tag.getCompound(NBT_TYPE));
            return BlockHelpers.getItemStackFromBlockState(blockState);
        }
        return null;
    }

    public static void setDisplayStandType(ItemStack displayStandStack, ItemStack type) {
        CompoundTag tag = displayStandStack.getOrCreateTag();
        tag.put(NBT_TYPE, BlockHelpers.serializeBlockState(BlockHelpers.getBlockStateFromItemStack(type)));
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        // Force allow right clicking with a fluid container passing through to this block
        if (!event.getItemStack().isEmpty()
                && event.getItemStack().getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent()
                && event.getLevel().getBlockState(event.getPos()).getBlock() == this) {
            event.setUseBlock(Event.Result.ALLOW);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult p_225533_6_) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntityDisplayStand tile = BlockEntityHelpers.get(world, pos, BlockEntityDisplayStand.class).orElse(null);
            if (tile != null) {
                ItemStack tileStack = tile.getInventory().getItem(0);
                if ((itemStack.isEmpty() || (ItemStack.isSameItemSameTags(itemStack, tileStack) && tileStack.getCount() < tileStack.getMaxStackSize())) && !tileStack.isEmpty()) {
                    if(!itemStack.isEmpty()) {
                        tileStack.grow(itemStack.getCount());
                    }
                    player.getInventory().setItem(player.getInventory().selected, tileStack);
                    tile.getInventory().setItem(0, ItemStack.EMPTY);
                    tile.sendUpdate();
                    return InteractionResult.SUCCESS;
                } else if (!itemStack.isEmpty() && tile.getInventory().getItem(0).isEmpty()) {
                    tile.getInventory().setItem(0, itemStack.split(1));
                    if (itemStack.getCount() <= 0)
                        player.getInventory().setItem(player.getInventory().selected, ItemStack.EMPTY);
                    tile.sendUpdate();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        ItemStack blockType = getDisplayStandType(stack);
        if (blockType != null) {
            tooltip.add(((MutableComponent) blockType.getHoverName())
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return BlockHelpers.doesBlockHaveSolidTopSurface(worldIn, pos);
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
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (!stateIn.canSurvive(worldIn, currentPos)) {
            worldIn.scheduleTick(currentPos, this, 1);
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void onRemove(BlockState oldState, Level world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide() && oldState.getBlock() != newState.getBlock()) {
            BlockEntityHelpers.get(world, blockPos, BlockEntityDisplayStand.class)
                    .ifPresent(tile -> InventoryHelpers.dropItems(world, tile.getInventory(), blockPos));
        }
        super.onRemove(oldState, world, blockPos, newState, isMoving);
    }
}
