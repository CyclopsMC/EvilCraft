package evilcraft.render.entity;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;
import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.ResourceLocation;

/**
 * Renderer for a netherfish
 * 
 * @author rubensworks
 *
 */
public class RenderNetherfish extends RenderSilverfish {
    
    private ResourceLocation texture;
    
    public RenderNetherfish(ExtendedConfig config) {
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.NAMEDID + ".png");
    }
    
    protected ResourceLocation getSilverfishTextures(EntitySilverfish par1EntitySilverfish) {
        return texture;
    }

}
