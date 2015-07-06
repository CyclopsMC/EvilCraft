package evilcraft.modcompat.thaumcraft;

import evilcraft.Configs;
import evilcraft.IInitListener;
import evilcraft.Reference;
import evilcraft.modcompat.IModCompat;

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
    public void onInit(Step step) {
        if(step == IInitListener.Step.PREINIT) {
            Configs.getInstance().configs.add(new VeinedScribingToolsConfig());
            Configs.getInstance().configs.add(new BloodWandCapConfig());
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
