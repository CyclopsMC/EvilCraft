package org.cyclops.evilcraft.core.config.configurable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainerGui;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;

import java.util.List;

/**
 * Block that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockContainerGuiTankInfo extends ConfigurableBlockContainerGui implements IInformationProvider, IBlockTank {
	
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     * @param tileEntity The class of the tile entity this blockState holds.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockContainerGuiTankInfo(ExtendedConfig eConfig,
            Material material, Class<? extends TankInventoryTileEntity> tileEntity) {
        super(eConfig, material, tileEntity);
    }
    
    @Override
    public boolean hasComparatorInputOverride(IBlockState blockState) {
    	return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos blockPos) {
    	TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(blockPos);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(MinecraftHelpers.COMPARATOR_MULTIPLIER * output);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, EnumFacing side, float motionX, float motionY, float motionZ) {
    	return BlockTankHelpers.onBlockActivatedTank(world, blockPos, player, hand, side, motionX, motionY, motionZ) ||
                super.onBlockActivated(world, blockPos, blockState, player, hand, side, motionX, motionY, motionZ);
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return BlockTankHelpers.getInfoTank(itemStack);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        
    }
	
	@Override
	public boolean isActivatable() {
		return false;
	}
    
    @Override
	public ItemStack toggleActivation(ItemStack itemStack, World world, EntityPlayer player) {
		return itemStack;
	}

	@Override
	public boolean isActivated(ItemStack itemStack, World world, Entity entity) {
		return false;
	}

    @Override
    protected ItemStack tileDataToItemStack(CyclopsTileEntity tile, ItemStack itemStack) {
        return BlockTankHelpers.tileDataToItemStack(tile, itemStack);
    }

}
