package org.cyclops.evilcraft.modcompat.tconstruct;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.cyclops.cyclopscore.init.IInitListener;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import org.cyclops.evilcraft.item.EnderTearConfig;
import slimeknights.tconstruct.library.TinkerRegistry;

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
    		EvilCraft._instance.getRegistryManager().getRegistry(IBloodChestRepairActionRegistry.class).
    			register(new TConstructToolRepairTickAction());
            Fluid ender = FluidRegistry.getFluid("Ender");
            if(Configs.isEnabled(EnderTearConfig.class) && ender != null) {
                TinkerRegistry.registerMelting(new ItemStack(EnderTearConfig._instance.getItemInstance()), ender,
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
