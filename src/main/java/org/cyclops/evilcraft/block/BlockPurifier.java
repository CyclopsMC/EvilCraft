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
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;
import org.cyclops.evilcraft.tileentity.TilePurifier;

/**
 * Block that can remove bad enchants from items.
 * @author rubensworks
 *
 */
public class BlockPurifier extends BlockTile {

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
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
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

}
