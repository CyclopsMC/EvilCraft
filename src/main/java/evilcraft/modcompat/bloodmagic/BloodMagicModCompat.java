package evilcraft.modcompat.bloodmagic;

import evilcraft.Configs;
import evilcraft.IInitListener;
import evilcraft.Reference;
import evilcraft.modcompat.IModCompat;

/**
 * Compatibility plugin for Forestry.
 * @author rubensworks
 *
 */
public class BloodMagicModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_BLOODMAGIC;
    }

    @Override
    public void onInit(IInitListener.Step step) {
    	if(step == IInitListener.Step.PREINIT) {
	        Configs.getInstance().configs.add(new BoundBloodDropConfig());
    	}
    }
    
    @Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Bound Blood Drop item to directly drain Blood from your soul network.";
	}

}
