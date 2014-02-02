package evilcraft.entities.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import evilcraft.Reference;
import evilcraft.api.config.EntityConfig;
import evilcraft.items.LightningGrenade;
import evilcraft.proxies.ClientProxy;

public class EntityLightningGrenadeConfig extends EntityConfig {
    
    public static EntityLightningGrenadeConfig _instance;

    public EntityLightningGrenadeConfig() {
        super(
            Reference.ENTITY_LIGHTNINGGRENADE,
            "Lightning Grenade",
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
