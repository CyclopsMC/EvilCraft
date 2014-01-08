package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.RenderSnowball;
import evilcraft.Reference;
import evilcraft.api.config.DummyConfig;
import evilcraft.items.LightningGrenade;
import evilcraft.proxies.ClientProxy;

public class EntityLightningGrenadeConfig extends DummyConfig {
    
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
    
    @Override
    public void onRegistered() {
        ClientProxy.ENTITY_RENDERERS.put(EntityLightningGrenade.class, new RenderSnowball(LightningGrenade.getInstance()));
    }
    
}
