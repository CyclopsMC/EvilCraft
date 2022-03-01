package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;
import org.cyclops.evilcraft.core.block.IBlockTank;

import javax.annotation.Nullable;

/**
 * Block that can remove bad enchants from items.
 * @author rubensworks
 *
 */
public class BlockPurifier extends BlockWithEntity implements IBlockTank {

    private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = Shapes.join(
            Shapes.block(),
            Shapes.or(
                    box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D),
                    box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D),
                    box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D),
                    INSIDE),
            BooleanOp.ONLY_FIRST);

    public BlockPurifier(Block.Properties properties) {
        super(properties, BlockEntityPurifier::new);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_PURIFIER, new BlockEntityPurifier.Ticker());
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult p_225533_6_) {
        if(world.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemStack = player.getInventory().getSelected();
            BlockEntityPurifier tile = (BlockEntityPurifier) world.getBlockEntity(blockPos);
            if(tile != null) {
                if (itemStack.isEmpty() && !tile.getPurifyItem().isEmpty()) {
                    player.getInventory().setItem(player.getInventory().selected, tile.getPurifyItem());
                    tile.setPurifyItem(ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                } else if (itemStack.isEmpty() && !tile.getAdditionalItem().isEmpty()) {
                    player.getInventory().setItem(player.getInventory().selected, tile.getAdditionalItem());
                    tile.setAdditionalItem(ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                } else if (FluidUtil.interactWithFluidHandler(player, hand, world, blockPos, Direction.UP)) {
                    return InteractionResult.SUCCESS;
                }  else if(!itemStack.isEmpty() && tile.getActions().isItemValidForAdditionalSlot(itemStack) && tile.getAdditionalItem().isEmpty()) {
                    ItemStack copy = itemStack.copy();
                    copy.setCount(1);
                    tile.setAdditionalItem(copy);
                    itemStack.shrink(1);
                    return InteractionResult.SUCCESS;
                } else if(!itemStack.isEmpty() && tile.getActions().isItemValidForMainSlot(itemStack) && tile.getPurifyItem().isEmpty()) {
                    ItemStack copy = itemStack.copy();
                    copy.setCount(1);
                    tile.setPurifyItem(copy);
                    itemStack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.FAIL;
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return INSIDE;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos blockPos) {
        BlockEntityPurifier tile = (BlockEntityPurifier) world.getBlockEntity(blockPos);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(MinecraftHelpers.COMPARATOR_MULTIPLIER * output);
    }

    @Override
    public void onRemove(BlockState oldState, Level world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide() && oldState.getBlock() != newState.getBlock()) {
            BlockEntityHelpers.get(world, blockPos, BlockEntityPurifier.class)
                    .ifPresent(tile -> InventoryHelpers.dropItems(world, tile.getInventory(), blockPos));
        }
        super.onRemove(oldState, world, blockPos, newState, isMoving);
    }

    @Override
    public int getDefaultCapacity() {
        return FluidHelpers.BUCKET_VOLUME * BlockEntityPurifier.MAX_BUCKETS;
    }

    @Override
    public boolean isActivatable() {
        return false;
    }

    @Override
    public ItemStack toggleActivation(ItemStack itemStack, Level world, Player player) {
        return itemStack;
    }

    @Override
    public boolean isActivated(ItemStack itemStack, Level world) {
        return false;
    }
}
