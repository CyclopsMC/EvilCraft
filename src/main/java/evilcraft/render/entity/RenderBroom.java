package evilcraft.render.entity;

import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import evilcraft.core.config.EntityConfig;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.render.ModelRender;
import evilcraft.render.models.BroomModel;

/**
 * Renderer for a broom
 * 
 * @author immortaleeb
 *
 */
public class RenderBroom extends ModelRender<BroomModel> {
	
    /**
     * Make a new instance.
     * @param config
     */
	public RenderBroom(ExtendedConfig<EntityConfig> config) {
	    super(config);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
		GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        
        // Note: using entity.rotationYaw instead of yaw seems to fix some glitchyness when rendering
        // In case this causes other problems, you can replace it by the yaw again
        GL11.glRotatef(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        
        bindEntityTexture(entity);
        
        model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.08F);
        
        GL11.glPopMatrix();
	}

    @Override
    protected BroomModel constructModel() {
        return new BroomModel();
    }

}
