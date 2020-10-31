package org.cyclops.evilcraft.core.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.block.BlockTileGui;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;
import org.cyclops.evilcraft.core.tileentity.TankInventoryTileEntity;

import java.util.List;
import java.util.function.Supplier;

/**
 * Tank block with gui.
 * @author rubensworks
 *
 */
public abstract class BlockTileGuiTank extends BlockTileGui implements IInformationProvider, IBlockTank {

    public BlockTileGuiTank(Properties properties, Supplier<CyclopsTileEntity> tileEntitySupplier) {
        super(properties, tileEntitySupplier);
    }
    
    @Override
    public boolean hasComparatorInputOverride(BlockState blockState) {
    	return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos blockPos) {
    	TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(blockPos);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(MinecraftHelpers.COMPARATOR_MULTIPLIER * output);
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (BlockTankHelpers.onBlockActivatedTank(blockState, world, blockPos, player, hand, rayTraceResult)) {
            return ActionResultType.SUCCESS;
        }
    	return super.onBlockActivated(blockState, world, blockPos, player, hand, rayTraceResult);
    }
    
    @Override
    public ITextComponent getInfo(ItemStack itemStack) {
        return BlockTankHelpers.getInfoTank(itemStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void provideInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag iTooltipFlag) {

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

	// TODO
    /*@Override
    protected ItemStack tileDataToItemStack(CyclopsTileEntity tile, ItemStack itemStack) {
        return BlockTankHelpers.tileDataToItemStack(tile, itemStack);
    }*/

}
