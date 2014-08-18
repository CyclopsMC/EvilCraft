package evilcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.helpers.L10NHelpers;
import evilcraft.network.PacketHandler;
import evilcraft.network.packets.FartPacket;

/**
 * A {@link KeyHandler} which handles farts.
 * 
 * @author immortaleeb
 *
 */
@SideOnly(Side.CLIENT)
public class FartKeyHandler implements KeyHandler {
	
	private boolean fartingEnabled = false;
	
	@Override
	public void onKeyPressed(KeyBinding kb) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		
		if (kb == Keys.FART.keyBinding) {
			fartingEnabled = !fartingEnabled;
			
			if (fartingEnabled)
				player.addChatComponentMessage(new ChatComponentText(L10NHelpers.localize("chat.command.fartingEnabled")));
			else
				player.addChatComponentMessage(new ChatComponentText(L10NHelpers.localize("chat.command.fartingDisabled")));
		}
		
		if (fartingEnabled && kb == settings.keyBindSneak) {
			PacketHandler.sendToServer(new FartPacket(player));
		}
	}
	
}
