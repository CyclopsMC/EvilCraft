package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.tileentity.TilePurifier;

/**
 * Block that can remove bad enchants from items.
 * @author rubensworks
 *
 */
public class BlockPurifier extends BlockTile implements IBlockTank {

    private static final VoxelShape INSIDE = makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(
            VoxelShapes.fullCube(),
            VoxelShapes.or(
                    makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D),
                    makeCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D),
                    makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D),
                    INSIDE),
            IBooleanFunction.ONLY_FIRST);

    public BlockPurifier(Block.Properties properties) {
        super(properties, TilePurifier::new);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult p_225533_6_) {
        if(world.isRemote()) {
            return ActionResultType.SUCCESS;
        } else {
            ItemStack itemStack = player.inventory.getCurrentItem();
            TilePurifier tile = (TilePurifier) world.getTileEntity(blockPos);
            if(tile != null) {
                if (itemStack.isEmpty() && !tile.getPurifyItem().isEmpty()) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getPurifyItem());
                    tile.setPurifyItem(ItemStack.EMPTY);
                    return ActionResultType.SUCCESS;
                } else if (itemStack.isEmpty() && !tile.getAdditionalItem().isEmpty()) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getAdditionalItem());
                    tile.setAdditionalItem(ItemStack.EMPTY);
                    return ActionResultType.SUCCESS;
                } else if (FluidUtil.interactWithFluidHandler(player, hand, world, blockPos, Direction.UP)) {
                    return ActionResultType.SUCCESS;
                }  else if(!itemStack.isEmpty() && tile.getActions().isItemValidForAdditionalSlot(itemStack) && tile.getAdditionalItem().isEmpty()) {
                    ItemStack copy = itemStack.copy();
                    copy.setCount(1);
                    tile.setAdditionalItem(copy);
                    itemStack.shrink(1);
                    return ActionResultType.SUCCESS;
                } else if(!itemStack.isEmpty() && tile.getActions().isItemValidForMainSlot(itemStack) && tile.getPurifyItem().isEmpty()) {
                    ItemStack copy = itemStack.copy();
                    copy.setCount(1);
                    tile.setPurifyItem(copy);
                    itemStack.shrink(1);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.FAIL;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return INSIDE;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos blockPos) {
        TilePurifier tile = (TilePurifier) world.getTileEntity(blockPos);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(MinecraftHelpers.COMPARATOR_MULTIPLIER * output);
    }

    @Override
    public void onReplaced(BlockState oldState, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (!world.isRemote() && oldState.getBlock() != newState.getBlock()) {
            TileHelpers.getSafeTile(world, blockPos, TilePurifier.class)
                    .ifPresent(tile -> InventoryHelpers.dropItems(world, tile.getInventory(), blockPos));
        }
        super.onReplaced(oldState, world, blockPos, newState, isMoving);
    }

    @Override
    public int getDefaultCapacity() {
        return FluidHelpers.BUCKET_VOLUME * TilePurifier.MAX_BUCKETS;
    }

    @Override
    public boolean isActivatable() {
        return false;
    }

    @Override
    public ItemStack toggleActivation(ItemStack itemStack, World world, PlayerEntity player) {
        return itemStack;
    }

    @Override
    public boolean isActivated(ItemStack itemStack, World world) {
        return false;
    }
}
