package evilcraft.modcompat.baubles;

import evilcraft.Reference;
import evilcraft.modcompat.IModCompat;
import evilcraft.modcompat.ModCompatLoader;

/**
 * Mod compat for the Baubles mod.
 * @author rubensworks
 *
 */
public class BaublesModCompat implements IModCompat {
	
	private static boolean canBeUsed = false;

	@Override
	public void onInit(Step initStep) {
		if(initStep == Step.PREINIT) {
			canBeUsed = ModCompatLoader.shouldLoadModCompat(this);
		}
	}

	@Override
	public String getModID() {
		return Reference.MOD_BAUBLES;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Vengeance Ring bauble.";
	}
	
	/**
	 * @return If the baubles mod compat can be used.
	 */
	public static boolean canUse() {
		return canBeUsed;
	}

}
