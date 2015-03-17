package evilcraft.modcompat.versionchecker;

import evilcraft.Reference;
import evilcraft.VersionStats;
import evilcraft.modcompat.IModCompat;
import evilcraft.modcompat.ModCompatLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

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
			canBeUsed = ModCompatLoader.shouldLoadModCompat(this);
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
	 * @param versionStats The version info holder.
	 */
	public static synchronized void sendIMCOutdatedMessage(VersionStats versionStats) {
		if(canBeUsed) {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("modDisplayName", Reference.MOD_NAME);
			compound.setString("oldVersion", Reference.MOD_VERSION);
			compound.setString("newVersion", versionStats.mod_version);

			compound.setString("updateUrl", versionStats.update_link);
			compound.setBoolean("isDirectLink", true);
			compound.setString("changeLog", "");

			FMLInterModComms.sendRuntimeMessage(Reference.MOD_ID, 
					Reference.MOD_VERSION_CHECKER, "addUpdate", compound);
		}
	}

}
