package org.cyclops.evilcraft.client.key;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.key.IKeyHandler;
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
    public void onKeyPressed(KeyMapping kb) {
        LocalPlayer player = Minecraft.getInstance().player;
        Options settings = Minecraft.getInstance().options;

        if (kb == Keys.FART) {
            fartingEnabled = !fartingEnabled;

            if (fartingEnabled)
                player.displayClientMessage(Component.translatable("chat.evilcraft.command.farting_enabled")
                        .withStyle(ChatFormatting.DARK_RED), true);
            else
                player.displayClientMessage(Component.translatable("chat.evilcraft.command.farting_disabled")
                        .withStyle(ChatFormatting.DARK_RED), true);
        }

        if (fartingEnabled && kb == settings.keyShift) {
            EvilCraft._instance.getPacketHandler().sendToServer(new FartPacket(player));
        }
    }

}
