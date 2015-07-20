package org.cyclops.evilcraft.modcompat.versionchecker;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.cyclopscore.tracking.IModVersion;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Mod compat for the Version Checker mod.
 * @author rubensworks
 *
 */
public class VersionCheckerModCompat implements IModCompat {
	
	private static boolean canBeUsed = false;

	@Override
	public void onInit(Step initStep) {
		if(initStep == Step.PREINIT) {
			canBeUsed = EvilCraft._instance.getModCompatLoader().shouldLoadModCompat(this);
		}
	}

	@Override
	public String getModID() {
		return Reference.MOD_VERSION_CHECKER;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Version Checker mod support.";
	}

	/**
	 * Send a message to the Version Checker mod with the update info.
	 * This is an integration with Dynious Version Checker See
	 * http://www.minecraftforum.net/topic/2721902-
	 * @param modVersion The mod version holder.
	 */
	public static synchronized void sendIMCOutdatedMessage(IModVersion modVersion) {
		if(canBeUsed) {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("modDisplayName", Reference.MOD_NAME);
			compound.setString("oldVersion", Reference.MOD_VERSION);
			compound.setString("newVersion", modVersion.getVersion());

			compound.setString("updateUrl", modVersion.getUpdateUrl());
			compound.setBoolean("isDirectLink", true);
			compound.setString("changeLog", modVersion.getInfo());

			FMLInterModComms.sendRuntimeMessage(Reference.MOD_ID, 
					Reference.MOD_VERSION_CHECKER, "addUpdate", compound);
		}
	}

}
