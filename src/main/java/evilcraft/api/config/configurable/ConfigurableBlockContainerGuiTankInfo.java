package evilcraft.api.config.configurable;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.entities.tileentitites.TankInventoryTileEntity;
import evilcraft.api.item.DamageIndicatedItemComponent;

/**
 * Block that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockContainerGuiTankInfo extends ConfigurableBlockContainerGui implements IInformationProvider {

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     * @param tileEntity The class of the tile entity this block holds.
     * @param guiID The unique ID for the GUI this block has.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockContainerGuiTankInfo(ExtendedConfig eConfig,
            Material material, Class<? extends TankInventoryTileEntity> tileEntity) {
        super(eConfig, material, tileEntity);
    }
    
    /**
     * Get the NBT name for the inner tank.
     * @return The NBT key for the tank.
     */
    public abstract String getTankNBTName();
    /**
     * Get the maximal tank capacity.
     * @return The maximal tank capacity in mB.
     */
    public abstract int getTankCapacity();
    
    @Override
    public String getInfo(ItemStack itemStack) {
        int amount = 0;
        if(itemStack.getTagCompound() != null) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound().getCompoundTag(getTankNBTName()));
            if(fluid != null)
                amount = fluid.amount;
        }
        return DamageIndicatedItemComponent.getInfo(amount, getTankCapacity());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        
    }

}
