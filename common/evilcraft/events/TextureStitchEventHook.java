package evilcraft.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.BlockFluid;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.RenderHelpers;

public class TextureStitchEventHook {
    
    protected static final String EMPTY_ICON_NAME = "empty";
    
    public static Map<Fluid, BlockFluidBase> fluidMap = new HashMap<Fluid, BlockFluidBase>();
    
    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void onTextureHookPre(TextureStitchEvent.Pre event) {
        RenderHelpers.EMPTYICON = event.map.registerIcon(Reference.MOD_ID+":"+EMPTY_ICON_NAME);
    }
    
    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void postStitch(TextureStitchEvent.Post event) {
        for(Entry<Fluid, BlockFluidBase> fluids : fluidMap.entrySet()) {
            fluids.getKey().setIcons(fluids.getValue().getBlockTextureFromSide(0), fluids.getValue().getBlockTextureFromSide(1));
        }
    }
}
