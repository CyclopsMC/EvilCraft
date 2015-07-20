package evilcraft.modcompat.tconstruct;

import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import org.cyclops.evilcraft.item.EnderTearConfig;
import evilcraft.modcompat.IModCompat;
import net.minecraft.item.ItemStack;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.Smeltery;

/**
 * Compatibility plugin for Tinkers' Construct.
 * @author rubensworks
 *
 */
public class TConstructModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_TCONSTRUCT;
    }

    @Override
    public void onInit(IInitListener.Step step) {
    	if(step == IInitListener.Step.POSTINIT) {
    		RegistryManager.getRegistry(IBloodChestRepairActionRegistry.class).
    			register(new TConstructToolRepairTickAction());
            if(Configs.isEnabled(EnderTearConfig.class)) {
                Smeltery.addMelting(FluidType.getFluidType("Ender"), new ItemStack(EnderTearConfig._instance.getItemInstance()), 0,
                        EnderTearConfig.mbLiquidEnder);
            }
    	}
    }
    
    @Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Blood chest repair support for tinker tools.";
	}

}
