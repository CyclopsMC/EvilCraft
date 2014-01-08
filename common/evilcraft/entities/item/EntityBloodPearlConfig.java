package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.RenderSnowball;
import evilcraft.Reference;
import evilcraft.api.config.DummyConfig;
import evilcraft.items.BloodPearlOfTeleportation;
import evilcraft.proxies.ClientProxy;

public class EntityBloodPearlConfig extends DummyConfig {
    
    public static EntityBloodPearlConfig _instance;

    public EntityBloodPearlConfig() {
        super(
            Reference.ENTITY_BLOODPEARL,
            "Blood Pearl of Teleportation",
            "entityBloodPearl",
            null,
            EntityBloodPearl.class
        );
    }
    
    @Override
    public void onRegistered() {
        ClientProxy.ENTITY_RENDERERS.put(EntityBloodPearl.class, new RenderSnowball(BloodPearlOfTeleportation.getInstance()));
    }
    
}
