package evilcraft.entities.effect;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.EntityConfig;
import evilcraft.api.render.RenderNull;

/**
 * Config for the {@link EntityAntiVengeanceBeam}.
 * @author rubensworks
 *
 */
public class EntityAntiVengeanceBeamConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityAntiVengeanceBeamConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityAntiVengeanceBeamConfig() {
        super(
        	true,
            "entityNeutronBeam",
            null,
            EntityAntiVengeanceBeam.class
        );
    }
    
    @Override
    public boolean isDisableable() {
    	return false;
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
