package evilcraft.entities.item;

import evilcraft.api.config.ExtendedConfig;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.RenderBroom;

public class EntityBroomConfig extends ExtendedConfig {
    
    public static EntityBroomConfig _instance;

    public EntityBroomConfig() {
        super(
            1,
            "EntityBroom",
            "entityBroom",
            null,
            EntityBroom.class
        );
    }
    
    @Override
    public void onRegistered() {
        ClientProxy.ENTITY_RENDERERS.put(EntityBroom.class, new RenderBroom());
    }
    
}
