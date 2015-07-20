package org.cyclops.evilcraft.modcompat.baubles;

import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

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
			canBeUsed = EvilCraft._instance.getModCompatLoader().shouldLoadModCompat(this);
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
		return "Vengeance Ring, Invigorating Pendant and Effortless Ring baubles.";
	}
	
	/**
	 * @return If the baubles mod compat can be used.
	 */
	public static boolean canUse() {
		return canBeUsed;
	}

}
