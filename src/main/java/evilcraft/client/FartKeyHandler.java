package evilcraft.client;

import evilcraft.network.PacketHandler;
import evilcraft.network.packet.FartPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.L10NHelpers;

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
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		
		if (kb == Keys.FART.keyBinding) {
			fartingEnabled = !fartingEnabled;
			
			if (fartingEnabled)
				player.addChatComponentMessage(new ChatComponentText(L10NHelpers.localize("chat.evilcraft.command.fartingEnabled")));
			else
				player.addChatComponentMessage(new ChatComponentText(L10NHelpers.localize("chat.evilcraft.command.fartingDisabled")));
		}
		
		if (fartingEnabled && kb == settings.keyBindSneak) {
			PacketHandler.sendToServer(new FartPacket(player));
		}
	}
	
}
