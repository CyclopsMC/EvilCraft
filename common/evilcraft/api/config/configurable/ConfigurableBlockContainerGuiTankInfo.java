package evilcraft.api.config.configurable;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.entities.tileentitites.TankInventoryTileEntity;
import evilcraft.api.item.DamageIndicatedItemComponent;
import evilcraft.blocks.BloodInfuserConfig;

public abstract class ConfigurableBlockContainerGuiTankInfo extends ConfigurableBlockContainerGui implements IInformationProvider {

    public ConfigurableBlockContainerGuiTankInfo(ExtendedConfig eConfig,
            Material material, Class<? extends TankInventoryTileEntity> tileEntity, int guiID) {
        super(eConfig, material, tileEntity, guiID);
    }
    
    public abstract String getTankNBTName();
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

}
