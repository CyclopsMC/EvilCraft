package evilcraft.render.entity;

import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.ResourceLocation;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.MobConfig;

/**
 * Renderer for a netherfish
 * 
 * @author rubensworks
 *
 */
public class RenderNetherfish extends RenderSilverfish {
    
    private ResourceLocation texture;
    
    /**
     * Make a new instance.
     * @param config Then config.
     */
    public RenderNetherfish(ExtendedConfig<MobConfig> config) {
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.NAMEDID + ".png");
    }
    
    @Override
    protected ResourceLocation getSilverfishTextures(EntitySilverfish entity) {
        return texture;
    }

}
