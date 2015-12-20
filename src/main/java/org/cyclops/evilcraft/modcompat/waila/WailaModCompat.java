package org.cyclops.evilcraft.modcompat.waila;

import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.cyclops.cyclopscore.init.IInitListener;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.evilcraft.Reference;

/**
 * Compatibility plugin for Waila.
 * @author rubensworks
 *
 */
public class WailaModCompat implements IModCompat {

    @Override
    public String getModID() {
        return Reference.MOD_WAILA;
    }

    @Override
    public void onInit(IInitListener.Step step) {
    	if(step == IInitListener.Step.INIT) {
    		FMLInterModComms.sendMessage(getModID(), "register", Waila.class.getName() + ".callbackRegister");
    	}
    }

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "WAILA tooltips on machines.";
	}

}
