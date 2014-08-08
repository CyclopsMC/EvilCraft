package evilcraft.modcompat.waila;

import cpw.mods.fml.common.event.FMLInterModComms;
import evilcraft.IInitListener;
import evilcraft.Reference;
import evilcraft.modcompat.IModCompat;

/**
 * Compatibility plugin for Waila.
 * @author rubensworks
 *
 */
public class WailaModCompat implements IModCompat {

    @Override
    public String getModID() {
        return Reference.MOD_WAILA;// + "disabled";
    }

    @Override
    public void onInit(IInitListener.Step step) {
    	if(step == IInitListener.Step.INIT) {
    		FMLInterModComms.sendMessage(getModID(), "register", Waila.class.getName() + ".callbackRegister");
    	}
    }

}
