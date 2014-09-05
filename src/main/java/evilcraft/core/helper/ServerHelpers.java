package evilcraft.core.helper;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.server.management.UserListOpsEntry;

/**
 * Contains helper methods for things involving minecraft servers.
 * @author immortaleeb
 *
 */
public class ServerHelpers {
	/**
     * Check if a command sender is an operator.
     * @param sender The command sender.
     * @return If this sender is an OP.
     */
    public static boolean isOp(ICommandSender sender) {
    	if(!sender.getEntityWorld().isRemote) {
    		return true;
    	}
    	int op_level = 4;
    	ServerConfigurationManager configManager = MinecraftServer.getServer().getConfigurationManager();
    	EntityPlayerMP player = configManager.func_152612_a(sender.getCommandSenderName());
    	if (configManager.func_152596_g(player.getGameProfile())) {
            UserListOpsEntry userlistopsentry = (UserListOpsEntry) configManager.func_152603_m()
            		.func_152683_b(player.getGameProfile());
            return userlistopsentry != null ? userlistopsentry.func_152644_a() >= op_level
            		: player.mcServer.getOpPermissionLevel() >= op_level;
        }
    	return false;
    }
}
