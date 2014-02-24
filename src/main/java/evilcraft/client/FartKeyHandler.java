package evilcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.network.FartPacket;

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
				player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("chat.command.fartingEnabled")));
			else
				player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("chat.command.fartingDisabled")));
		}
		
		if (fartingEnabled && kb == settings.keyBindSneak) {
			EvilCraft.channel.sendToServer(new FartPacket());
		}
	}
	
}
