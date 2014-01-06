package evilcraft.entities.item;

import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

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
        //EvilCraft.renderers.put(EntityBroom.class, EntityBoat.));
    }
    
}
