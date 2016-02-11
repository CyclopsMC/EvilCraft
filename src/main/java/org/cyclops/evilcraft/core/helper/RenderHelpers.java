package org.cyclops.evilcraft.core.helper;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A helper for rendering.
 * @author rubensworks
 *
 */
@SideOnly(Side.CLIENT)
public class RenderHelpers {
    
    /**
     * An icon that contains to texture, useful for when you want to render nothing.
     */
    public static TextureAtlasSprite EMPTYICON;

    /**
     * Texture sheet of all blocks.
     */
    public static ResourceLocation TEXTURE_MAP = TextureMap.locationBlocksTexture;
}
