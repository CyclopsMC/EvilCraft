package org.cyclops.evilcraft.core.helper;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A helper for rendering.
 * @author rubensworks
 *
 */
@OnlyIn(Dist.CLIENT)
public class RenderHelpers {
    
    /**
     * An icon that contains to texture, useful for when you want to render nothing.
     */
    public static TextureAtlasSprite EMPTYICON;

    /**
     * Texture sheet of all blocks.
     */
    public static ResourceLocation TEXTURE_MAP = AtlasTexture.LOCATION_BLOCKS;
}
