package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.entity.monster.Netherfish;

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
     * @param renderManager The render manager.
     * @param config Then config.
     */
    public RenderNetherfish(RenderManager renderManager, ExtendedConfig<MobConfig<Netherfish>> config) {
        super(renderManager);
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntitySilverfish entity) {
        return texture;
    }

}
