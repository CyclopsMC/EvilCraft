package org.cyclops.evilcraft.modcompat.thaumcraft;

import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.init.IInitListener;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Compatibility plugin for Thaumcraft.
 * @author rubensworks
 *
 */
public class ThaumcraftModCompat implements IModCompat {

    @Override
    public String getModID() {
        return Reference.MOD_THAUMCRAFT;
    }

    @Override
    public void onInit(IInitListener.Step step) {
        if(step == IInitListener.Step.PREINIT) {
            ConfigHandler configs = EvilCraft._instance.getConfigHandler();
            configs.add(new VeinedScribingToolsConfig());
            configs.add(new BloodWandCapConfig());
        } else if(step == Step.INIT) {
            Thaumcraft.register();
    	}
    }

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Adds Thaumcraft aspects to EvilCraft items and blocks, a new Blood Wand Cap, Vein Scribing Tools and extra Loot Bag items.";
	}

}
