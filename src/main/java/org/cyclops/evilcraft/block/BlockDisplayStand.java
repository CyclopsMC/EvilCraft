package org.cyclops.evilcraft.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.tileentity.TileDisplayStand;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A stand for displaying items.
 * @author rubensworks
 *
 */
public class BlockDisplayStand extends BlockTile {

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
        super(properties, TileDisplayStand::new);
        MinecraftForge.EVENT_BUS.register(this);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(AXIS_X, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, AXIS_X);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return FACING_BOUNDS.get(BlockHelpers.getSafeBlockStateProperty(state, FACING, Direction.DOWN));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, World world, BlockPos blockPos) {
        return TileHelpers.getSafeTile(world, blockPos, TileDisplayStand.class)
                .map(tile -> !tile.getInventory().getItem(0).isEmpty() ? 15 : 0)
                .orElse(0);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
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
    public void setPlacedBy(World world, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(world, blockPos, blockState, entity, stack);
        if (!world.isClientSide()) {
            TileHelpers.getSafeTile(world, blockPos, TileDisplayStand.class)
                    .ifPresent(tile -> {
                        tile.setDisplayStandType(getDisplayStandType(stack));
                        tile.setDirection(entity.getDirection().getAxisDirection());
                    });
        }
    }

    @Override
    public BlockState rotate(BlockState blockState, IWorld world, BlockPos pos, Rotation direction) {
        return TileHelpers.getSafeTile(world, pos, TileDisplayStand.class)
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

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> list) {
        try {
            for (Item item : ItemTags.PLANKS.getValues()) {
                if (item instanceof BlockItem) {
                    BlockState plankWoodBlockState = BlockHelpers.getBlockStateFromItemStack(new ItemStack(item));
                    list.add(getTypedDisplayStandItem(plankWoodBlockState));
                }
            }
        } catch (IllegalStateException e) {
            // Can occur during mod loading when the tag has not been set yet
        }
    }

    public ItemStack getTypedDisplayStandItem(BlockState blockState) {
        INBT blockTag = BlockHelpers.serializeBlockState(blockState);
        ItemStack itemStack = new ItemStack(this);
        CompoundNBT tag = itemStack.getOrCreateTag();
        tag.put(NBT_TYPE, blockTag);
        return itemStack;
    }

    public ItemStack getDisplayStandType(ItemStack displayStandStack) {
        CompoundNBT tag = displayStandStack.getTag();
        if (tag != null && tag.contains(NBT_TYPE)) {
            BlockState blockState = BlockHelpers.deserializeBlockState(tag.getCompound(NBT_TYPE));
            return BlockHelpers.getItemStackFromBlockState(blockState);
        }
        return null;
    }

    public static void setDisplayStandType(ItemStack displayStandStack, ItemStack type) {
        CompoundNBT tag = displayStandStack.getOrCreateTag();
        tag.put(NBT_TYPE, BlockHelpers.serializeBlockState(BlockHelpers.getBlockStateFromItemStack(type)));
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        // Force allow right clicking with a fluid container passing through to this block
        if (!event.getItemStack().isEmpty()
                && event.getItemStack().getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).isPresent()
                && event.getWorld().getBlockState(event.getPos()).getBlock() == this) {
            event.setUseBlock(Event.Result.ALLOW);
        }
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult p_225533_6_) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (world.isClientSide()) {
            return ActionResultType.SUCCESS;
        } else {
            TileDisplayStand tile = TileHelpers.getSafeTile(world, pos, TileDisplayStand.class).orElse(null);
            if (tile != null) {
                ItemStack tileStack = tile.getInventory().getItem(0);
                if ((itemStack.isEmpty() || (ItemStack.isSame(itemStack, tileStack) && ItemStack.tagMatches(itemStack, tileStack) && tileStack.getCount() < tileStack.getMaxStackSize())) && !tileStack.isEmpty()) {
                    if(!itemStack.isEmpty()) {
                        tileStack.grow(itemStack.getCount());
                    }
                    player.inventory.setItem(player.inventory.selected, tileStack);
                    tile.getInventory().setItem(0, ItemStack.EMPTY);
                    tile.sendUpdate();
                    return ActionResultType.SUCCESS;
                } else if (!itemStack.isEmpty() && tile.getInventory().getItem(0).isEmpty()) {
                    tile.getInventory().setItem(0, itemStack.split(1));
                    if (itemStack.getCount() <= 0)
                        player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
                    tile.sendUpdate();
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        ItemStack blockType = getDisplayStandType(stack);
        if (blockType != null) {
            tooltip.add(((IFormattableTextComponent) blockType.getHoverName())
                    .withStyle(TextFormatting.GRAY));
        }
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return BlockHelpers.doesBlockHaveSolidTopSurface(worldIn, pos);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isAreaLoaded(pos, 1))
            return;
        if (!state.canSurvive(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (!stateIn.canSurvive(worldIn, currentPos)) {
            worldIn.getBlockTicks().scheduleTick(currentPos, this, 1);
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void onRemove(BlockState oldState, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide() && oldState.getBlock() != newState.getBlock()) {
            TileHelpers.getSafeTile(world, blockPos, TileDisplayStand.class)
                    .ifPresent(tile -> InventoryHelpers.dropItems(world, tile.getInventory(), blockPos));
        }
        super.onRemove(oldState, world, blockPos, newState, isMoving);
    }
}
