package evilcraft.client.key;

import evilcraft.EvilCraft;
import evilcraft.network.packet.FartPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.client.key.IKeyHandler;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.network.PacketHandler;

/**
 * A {@link IKeyHandler} which handles farts.
 * 
 * @author immortaleeb
 *
 */
@SideOnly(Side.CLIENT)
public class FartKeyHandler implements IKeyHandler {
	
	private boolean fartingEnabled = false;
	
	@Override
	public void onKeyPressed(KeyBinding kb) {
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		
		if (kb == Keys.FART) {
			fartingEnabled = !fartingEnabled;
			
			if (fartingEnabled)
				player.addChatComponentMessage(new ChatComponentText(L10NHelpers.localize("chat.evilcraft.command.fartingEnabled")));
			else
				player.addChatComponentMessage(new ChatComponentText(L10NHelpers.localize("chat.evilcraft.command.fartingDisabled")));
		}
		
		if (fartingEnabled && kb == settings.keyBindSneak) {
			EvilCraft._instance.getPacketHandler().sendToServer(new FartPacket(player));
		}
	}
	
}
