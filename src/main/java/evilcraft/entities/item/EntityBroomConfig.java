package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.EntityConfig;
import evilcraft.render.entity.RenderBroom;

/**
 * Config for the {@link EntityBroom}.
 * @author rubensworks
 *
 */
public class EntityBroomConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityBroomConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityBroomConfig() {
        super(
        	true,
            "broom",
            null,
            EntityBroom.class
        );
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    protected Render getRender() {
        return new RenderBroom(this);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
    @Override
    public int getUpdateFrequency() {
        return 10;
    }
}
