package org.cyclops.evilcraft.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.helper.RenderHelpers;

/**
 * Event hook for {@link TextureStitchEventHook}.
 * @author rubensworks
 *
 */
public class TextureStitchEventHook {

    protected static final String EMPTY_ICON_NAME = "empty";

    /**
     * Before the texture stitching.
     * @param event The received event.
     */
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onTextureHookPre(TextureStitchEvent.Pre event) {
        RenderHelpers.EMPTYICON = event.getAtlas().getSprite(new ResourceLocation(Reference.MOD_ID, EMPTY_ICON_NAME));
    }
}
