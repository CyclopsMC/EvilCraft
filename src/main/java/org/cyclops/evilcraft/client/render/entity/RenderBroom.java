package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.entity.item.EntityBroom;

/**
 * Renderer for a broom
 * 
 * @author immortaleeb
 *
 */
public class RenderBroom extends Render<EntityBroom> {
	
    /**
     * Make a new instance.
     * @param renderManager The render manager
     * @param config The config.
     */
	public RenderBroom(RenderManager renderManager, ExtendedConfig<EntityConfig> config) {
	    super(renderManager);
	}

    protected ItemStack getItemStack(EntityBroom entity) {
        return entity.getBroomStack();
    }

	@Override
	public void doRender(EntityBroom entity, double x, double y, double z, float yaw, float partialTickTime) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.2F, z);
        GlStateManager.scale(2, 2, 2);
        
        // Note: using entity.rotationYaw instead of yaw seems to fix some glitchyness when rendering
        // In case this causes other problems, you can replace it by the yaw again
        float rotationYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTickTime;
        float rotationPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTickTime;
        GlStateManager.rotate(-rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(rotationPitch, 1.0F, 0.0F, 0.0F);
        
        bindEntityTexture(entity);

        RenderHelpers.renderItem(getItemStack(entity));
        
        GlStateManager.popMatrix();
	}

    @Override
    protected ResourceLocation getEntityTexture(EntityBroom entity) {
        return null;
    }

}
