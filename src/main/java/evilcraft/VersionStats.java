package evilcraft;

import java.io.IOException;
import java.net.URL;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * This will execute the version checker.
 * @author rubensworks
 *
 */
public class VersionStats {
	
	private static boolean CHECKED = false;
	
	/**
	 * Latest mod version ID.
	 */
	public String mod_version;
	/**
	 * Download URL of latest mod version.
	 */
	public String update_link;
	
	private VersionStats() {
		
	}

	/**
	 * Get the version of this mod.
	 * @return The latest version.
	 */
	public static String getVersion() {
		return Reference.MOD_VERSION + " for Minecraft " + Reference.MOD_MC_VERSION;
	}
	
	/**
	 * Check the latest version.
	 * @param event The tick event.
	 */
	public static synchronized void check(PlayerTickEvent event) {
		if(!CHECKED) {
			CHECKED = true;
			if(GeneralConfig.versionChecker) {
				EntityPlayer player = event.player;
				
				sendMessage(player, "Using "+Reference.MOD_NAME+" "+getVersion());
				
				if(GeneralConfig.version != Reference.MOD_VERSION) {
					sendMessage(player, "Your config file is out of date, this could cause problems.");
				}
				
				VersionStats versionStats = getVersionStats();
				if(needsUpdate(versionStats)) {
					sendMessage(player, "Version " + versionStats.mod_version + " is available at " + versionStats.update_link);
				}
			}
		}
	}
	
	private static boolean needsUpdate(VersionStats versionStats) {
		if(versionStats != null) {
			if(Reference.MOD_VERSION != versionStats.mod_version)
				return true;
		}
		return false;
	}
	
	private static void sendMessage(EntityPlayer player, String message) {
		player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + message));
	}
	
	private static VersionStats getVersionStats() {
		VersionStats versionStats = null;
		try {
			Gson gson = new Gson();
			String location = Reference.URL_VERSIONSTATS
					+ "?mc_version=" + Reference.MOD_MC_VERSION + "&mod_version=" + Reference.MOD_VERSION;
			versionStats = gson.fromJson(IOUtils.toString(new URL(location)), VersionStats.class);
		} catch (IOException e) {
			EvilCraft.log("Can't connect to version stats server");
		}
		return versionStats;
	}
	
}
