package evilcraft.events;

import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.RenderHelpers;
import evilcraft.blocks.LiquidBlockBlood;
import evilcraft.fluids.Blood;

public class TextureStitchEventHook {
    
    protected static final String EMPTY_ICON_NAME = "empty";
    
    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void onTextureHookPost(TextureStitchEvent.Post event) {
        if (event.map.textureType == 0) {
            Blood.getInstance().setIcons(LiquidBlockBlood.getInstance().getBlockTextureFromSide(1), LiquidBlockBlood.getInstance().getBlockTextureFromSide(2));
        }
    }
    
    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void onTextureHookPre(TextureStitchEvent.Pre event) {
        RenderHelpers.EMPTYICON = event.map.registerIcon(Reference.MOD_ID+":"+EMPTY_ICON_NAME);
    }
}
