package org.cyclops.evilcraft.client.key;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.client.key.IKeyHandler;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.network.packet.FartPacket;

/**
 * A {@link IKeyHandler} which handles farts.
 *
 * @author immortaleeb
 *
 */
public class FartKeyHandler implements IKeyHandler {

    private boolean fartingEnabled = false;

    @Override
    public void onKeyPressed(KeyMapping kb) {
        LocalPlayer player = Minecraft.getInstance().player;
        Options settings = Minecraft.getInstance().options;

        if (kb == Keys.FART) {
            fartingEnabled = !fartingEnabled;

            if (fartingEnabled)
                player.sendOverlayMessage(Component.translatable("chat.evilcraft.command.farting_enabled")
                        .withStyle(ChatFormatting.DARK_RED));
            else
                player.sendOverlayMessage(Component.translatable("chat.evilcraft.command.farting_disabled")
                        .withStyle(ChatFormatting.DARK_RED));
        }

        if (fartingEnabled && kb == settings.keyShift) {
            EvilCraft._instance.getPacketHandler().sendToServer(new FartPacket(player));
        }
    }

}
