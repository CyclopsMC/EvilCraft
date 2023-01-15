package org.cyclops.evilcraft.core.helper;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
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
     * Texture sheet of all blocks.
     */
    public static ResourceLocation TEXTURE_MAP = TextureAtlas.LOCATION_BLOCKS;
}
