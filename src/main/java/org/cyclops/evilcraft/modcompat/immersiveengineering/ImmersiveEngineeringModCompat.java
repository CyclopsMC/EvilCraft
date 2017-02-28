package org.cyclops.evilcraft.modcompat.immersiveengineering;

import org.cyclops.cyclopscore.init.IInitListener;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.evilcraft.Reference;

/**
 * Compatibility plugin for Immersive Engineering.
 * @author runesmacher
 *
 */
public class ImmersiveEngineeringModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_IMMERSIVEENGINEERING;
    }

    @Override
    public void onInit(IInitListener.Step step) {
        if(step == IInitListener.Step.INIT) {
            ImmersiveEngineeringRecipeManager.register();
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getComment() {
        return "squeezer support.";
    }

}
