package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import cpw.mods.fml.client.registry.RenderingRegistry;
import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.items.LightningGrenade;
import evilcraft.proxies.ClientProxy;

public class EntityLightningGrenadeConfig extends ExtendedConfig {
    
    public static EntityLightningGrenadeConfig _instance;

    public EntityLightningGrenadeConfig() {
        super(
            1,
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
