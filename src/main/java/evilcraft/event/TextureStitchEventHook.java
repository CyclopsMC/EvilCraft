package evilcraft.event;

import com.google.common.collect.Maps;
import evilcraft.Reference;
import evilcraft.core.helper.RenderHelpers;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockFluidClassic;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Event hook for {@link TextureStitchEventHook}.
 * @author rubensworks
 *
 */
public class TextureStitchEventHook {
    
    protected static final String EMPTY_ICON_NAME = "empty";
    
    /**
     * The mapping of {@link Fluid} to {@link org.cyclops.cyclopscore.config.configurable.ConfigurableBlockFluidClassic}.
     */
    public static Map<Fluid, ConfigurableBlockFluidClassic> fluidMap = Maps.newHashMap();
    
    /**
     * Before the texture stitching.
     * @param event The received event.
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureHookPre(TextureStitchEvent.Pre event) {
        RenderHelpers.EMPTYICON = event.map.getAtlasSprite(Reference.MOD_ID+":"+EMPTY_ICON_NAME);
    }
}
