package evilcraft.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.api.RenderHelpers;

/**
 * Event hook for {@link TextureStitchEventHook}.
 * @author rubensworks
 *
 */
public class TextureStitchEventHook {
    
    protected static final String EMPTY_ICON_NAME = "empty";
    
    /**
     * The mapping of {@link Fluid} to {@link BlockFluidBase}.
     */
    public static Map<Fluid, BlockFluidBase> fluidMap = new HashMap<Fluid, BlockFluidBase>();
    
    /**
     * Before the texture stitching.
     * @param event The received event.
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureHookPre(TextureStitchEvent.Pre event) {
        RenderHelpers.EMPTYICON = Minecraft.getMinecraft().getTextureMapBlocks().registerIcon(Reference.MOD_ID+":"+EMPTY_ICON_NAME);
    }
    
    /**
     * After the texture stitching.
     * @param event The received event.
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void postStitch(TextureStitchEvent.Post event) {
        for(Entry<Fluid, BlockFluidBase> fluids : fluidMap.entrySet()) {
            fluids.getKey().setIcons(fluids.getValue().getBlockTextureFromSide(0), fluids.getValue().getBlockTextureFromSide(1));
        }
    }
}
