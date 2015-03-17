package evilcraft.core.config.configurable;

import evilcraft.core.IInformationProvider;
import evilcraft.core.block.IBlockTank;
import evilcraft.core.block.component.BlockTankComponent;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.tileentity.TankInventoryTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

/**
 * Block that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockContainerGuiTankInfo extends ConfigurableBlockContainerGui implements IInformationProvider, IBlockTank {

	private BlockTankComponent<ConfigurableBlockContainerGuiTankInfo> tankComponent = 
			new BlockTankComponent<ConfigurableBlockContainerGuiTankInfo>(this); 
	
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
    public boolean hasComparatorInputOverride() {
    	return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos) {
    	TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(blockPos);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(MinecraftHelpers.COMPARATOR_MULTIPLIER * output);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumFacing side, float motionX, float motionY, float motionZ) {
    	return tankComponent.onBlockActivatedTank(world, blockPos, player, side, motionX, motionY, motionZ) ||
                super.onBlockActivated(world, blockPos, blockState, player, side, motionX, motionY, motionZ);
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return tankComponent.getInfoTank(itemStack);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        
    }
    
    @Override
	public void setTankCapacity(ItemStack itemStack, int capacity) {
		// Does nothing
	}

	@Override
	public void setTankCapacity(NBTTagCompound tag, int capacity) {
		// Does nothing
	}
	
	@Override
    public int getTankCapacity(ItemStack itemStack) {
		return getMaxCapacity();
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

}
