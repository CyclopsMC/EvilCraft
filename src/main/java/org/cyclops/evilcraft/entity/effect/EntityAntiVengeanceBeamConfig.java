package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.client.render.RenderNull;

/**
 * Config for the {@link EntityAntiVengeanceBeam}.
 * @author rubensworks
 *
 */
public class EntityAntiVengeanceBeamConfig extends EntityConfig<Entity> {
    
    /**
     * The unique instance.
     */
    public static EntityAntiVengeanceBeamConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityAntiVengeanceBeamConfig() {
        super(
                EvilCraft._instance,
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
    public Render<Entity> getRender(RenderManager renderManager, RenderItem renderItem) {
    	return new RenderNull(renderManager);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
