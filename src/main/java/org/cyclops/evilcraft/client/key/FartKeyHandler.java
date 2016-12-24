package org.cyclops.evilcraft.client.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.client.key.IKeyHandler;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.network.packet.FartPacket;

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
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		
		if (kb == Keys.FART) {
			fartingEnabled = !fartingEnabled;
			
			if (fartingEnabled)
				player.sendMessage(new TextComponentString(L10NHelpers.localize("chat.evilcraft.command.farting_enabled")));
			else
				player.sendMessage(new TextComponentString(L10NHelpers.localize("chat.evilcraft.command.farting_disabled")));
		}
		
		if (fartingEnabled && kb == settings.keyBindSneak) {
			EvilCraft._instance.getPacketHandler().sendToServer(new FartPacket(player));
		}
	}
	
}
