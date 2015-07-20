package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.item.Broom;

/**
 * Renderer for a broom
 * 
 * @author immortaleeb
 *
 */
public class RenderBroom extends Render {

    // TODO: temporary static way of rendering brooms.
    private static final ItemStack BROOM = new ItemStack(Broom.getInstance());
	
    /**
     * Make a new instance.
     * @param renderManager The render manager
     */
	public RenderBroom(RenderManager renderManager, ExtendedConfig<EntityConfig> config) {
	    super(renderManager);
	}

    protected ItemStack getItemStack(Entity entity) {
        return BROOM;
    }

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.2F, z);
        GlStateManager.scale(2, 2, 2);
        
        // Note: using entity.rotationYaw instead of yaw seems to fix some glitchyness when rendering
        // In case this causes other problems, you can replace it by the yaw again
        GlStateManager.rotate(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        
        bindEntityTexture(entity);

        Minecraft.getMinecraft().getRenderItem().renderItemModel(getItemStack(entity));
        
        GlStateManager.popMatrix();
	}

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

}
