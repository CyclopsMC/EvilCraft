package evilcraft.modcompat.waila;

import evilcraft.IInitListener;
import evilcraft.Reference;
import evilcraft.modcompat.IModCompat;
import net.minecraftforge.fml.common.event.FMLInterModComms;

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

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "WAILA tooltips on machines.";
	}

}
