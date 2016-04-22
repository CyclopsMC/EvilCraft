package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.LightningGrenade;

/**
 * Config for {@link EntityLightningGrenade}.
 * @author rubensworks
 *
 */
public class EntityLightningGrenadeConfig extends EntityConfig<EntityLightningGrenade> {
    
    /**
     * The unique instance.
     */
    public static EntityLightningGrenadeConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityLightningGrenadeConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entityLightningGrenade",
            null,
            EntityLightningGrenade.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render<EntityLightningGrenade> getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderSnowball<EntityLightningGrenade>(renderManager, LightningGrenade.getInstance(), renderItem);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
