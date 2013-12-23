package evilcraft.events;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.blocks.LiquidBlockBlood;
import evilcraft.fluids.Blood;

public class TextureStitchEventHook {
    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void textureHook(TextureStitchEvent.Post event) {
        if (event.map.textureType == 0) {
            Blood.getInstance().setIcons(LiquidBlockBlood.getInstance().getBlockTextureFromSide(1), LiquidBlockBlood.getInstance().getBlockTextureFromSide(2));
        }
    }
}
