package evilcraft.modcompat.tconstruct;

import evilcraft.IInitListener;
import evilcraft.Reference;
import evilcraft.entities.tileentities.tickaction.bloodchest.BloodChestRepairActionRegistry;
import evilcraft.modcompat.IModCompat;

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
    		BloodChestRepairActionRegistry.register(new TConstructToolRepairTickAction());
    	}
    }

}
