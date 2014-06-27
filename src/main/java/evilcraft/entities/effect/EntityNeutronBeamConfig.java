package evilcraft.entities.effect;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.EntityConfig;
import evilcraft.api.render.RenderNull;

/**
 * Config for the {@link EntityNeutronBeam}.
 * @author rubensworks
 *
 */
public class EntityNeutronBeamConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityNeutronBeamConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityNeutronBeamConfig() {
        super(
        	true,
            "entityNeutronBeam",
            null,
            EntityNeutronBeam.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
    	return new RenderNull();
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
