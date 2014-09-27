package evilcraft.modcompat.nei;

import evilcraft.Reference;
import evilcraft.modcompat.IModCompat;
import evilcraft.modcompat.ModCompatLoader;

/**
 * Config for the NEI integration of this mod.
 * @author rubensworks
 *
 */
public class NEIModCompat implements IModCompat {
	
	/**
	 * If the modcompat can be used.
	 */
	public static boolean canBeUsed = false;

	@Override
	public void onInit(Step initStep) {
		if(initStep == Step.PREINIT) {
			canBeUsed = ModCompatLoader.shouldLoadModCompat(this);
		}
	}

	@Override
	public String getModID() {
		return Reference.MOD_NEI;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Blood Infuser and Environmental Accumulator recipes.";
	}

}
