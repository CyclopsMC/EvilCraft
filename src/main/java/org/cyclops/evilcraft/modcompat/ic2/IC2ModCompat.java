package org.cyclops.evilcraft.modcompat.ic2;

import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.evilcraft.Reference;

/**
 * Compatibility plugin for IC2.
 * @author rubensworks
 *
 */
public class IC2ModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_IC2;
    }

    @Override
    public void onInit(Step step) {
    	if(step == Step.POSTINIT) {
            IC2.registerMaceratorRecipes();
        }
    }
    
    @Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Macerator recipe for Dark Ore and Dark Gem.";
	}

}
