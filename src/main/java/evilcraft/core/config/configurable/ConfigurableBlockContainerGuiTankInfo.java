package evilcraft.core.config.configurable;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import evilcraft.core.IInformationProvider;
import evilcraft.core.block.IBlockTank;
import evilcraft.core.block.component.BlockTankComponent;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.tileentity.TankInventoryTileEntity;

/**
 * Block that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockContainerGuiTankInfo extends ConfigurableBlockContainerGui implements IInformationProvider, IBlockTank {

	private BlockTankComponent<ConfigurableBlockContainerGuiTankInfo> tankComponent = 
			new BlockTankComponent<ConfigurableBlockContainerGuiTankInfo>(this); 
	
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     * @param tileEntity The class of the tile entity this block holds.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockContainerGuiTankInfo(ExtendedConfig eConfig,
            Material material, Class<? extends TankInventoryTileEntity> tileEntity) {
        super(eConfig, material, tileEntity);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float motionX, float motionY, float motionZ) {
    	if(tankComponent.onBlockActivatedTank(world, x, y, z, player, side, motionX, motionY, motionZ)) {
        	return true;
        }
    	return super.onBlockActivated(world, x, y, z, player, side, motionX, motionY, motionZ);
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
