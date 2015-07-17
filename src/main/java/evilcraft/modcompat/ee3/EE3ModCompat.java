package evilcraft.modcompat.ee3;

import evilcraft.Reference;
import evilcraft.modcompat.IModCompat;

/**
 * Compatibility plugin for EE3.
 * @author rubensworks
 *
 */
public class EE3ModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_EE3;
    }

    @Override
    public void onInit(Step step) {
    	if(step == Step.POSTINIT) {
            EE3.registerItems();
            EE3.registerCrafting();
        }
    }
    
    @Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "EMC values for all items and blocks.";
	}

}
