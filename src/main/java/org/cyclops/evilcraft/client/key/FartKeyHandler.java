package org.cyclops.evilcraft.client.key;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
@OnlyIn(Dist.CLIENT)
public class FartKeyHandler implements IKeyHandler {
	
	private boolean fartingEnabled = false;
	
	@Override
	public void onKeyPressed(KeyBinding kb) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		GameSettings settings = Minecraft.getInstance().options;
		
		if (kb == Keys.FART) {
			fartingEnabled = !fartingEnabled;
			
			if (fartingEnabled)
				player.displayClientMessage(new TranslationTextComponent("chat.evilcraft.command.farting_enabled")
						.withStyle(TextFormatting.DARK_RED), true);
			else
				player.displayClientMessage(new TranslationTextComponent("chat.evilcraft.command.farting_disabled")
						.withStyle(TextFormatting.DARK_RED), true);
		}
		
		if (fartingEnabled && kb == settings.keyShift) {
			EvilCraft._instance.getPacketHandler().sendToServer(new FartPacket(player));
		}
	}
	
}
