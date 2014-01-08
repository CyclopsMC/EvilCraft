package evilcraft.entities.item;

import evilcraft.Reference;
import evilcraft.api.config.DummyConfig;

public class EntityBroomConfig extends DummyConfig {
    
    public static EntityBroomConfig _instance;

    public EntityBroomConfig() {
        super(
            Reference.ENTITY_BROOM,
            "Broom",
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
