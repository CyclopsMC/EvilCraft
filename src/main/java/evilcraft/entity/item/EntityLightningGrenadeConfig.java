package evilcraft.entity.item;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.EntityConfig;
import evilcraft.item.LightningGrenade;

/**
 * Config for {@link EntityLightningGrenade}.
 * @author rubensworks
 *
 */
public class EntityLightningGrenadeConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityLightningGrenadeConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityLightningGrenadeConfig() {
        super(
        	true,
            "entityLightningGrenade",
            null,
            EntityLightningGrenade.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
        return new RenderSnowball(LightningGrenade.getInstance());
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
