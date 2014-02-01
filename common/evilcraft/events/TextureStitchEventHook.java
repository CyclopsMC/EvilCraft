package evilcraft.events;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.RenderHelpers;

public class TextureStitchEventHook {
    
    protected static final String EMPTY_ICON_NAME = "empty";
    
    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void onTextureHookPre(TextureStitchEvent.Pre event) {
        RenderHelpers.EMPTYICON = event.map.registerIcon(Reference.MOD_ID+":"+EMPTY_ICON_NAME);
    }
}
