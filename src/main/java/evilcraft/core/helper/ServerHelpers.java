package evilcraft.core.helper;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

/**
 * Contains helper methods for things involving minecraft servers.
 * @author immortaleeb, rubensworks
 *
 */
public class ServerHelpers {
	/**
     * Check if a command sender is an operator.
     * @param user The username.
     * @return If this sender is an OP.
     */
    public static boolean isOp(String user) {
    	ServerConfigurationManager configManager = MinecraftServer.getServer().getConfigurationManager();
        return configManager.getOppedPlayers().getGameProfileFromName(user) != null;
    }
}
