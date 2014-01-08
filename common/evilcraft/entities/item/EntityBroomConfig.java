package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBoat;
import evilcraft.Reference;
import evilcraft.api.config.EntityConfig;

public class EntityBroomConfig extends EntityConfig {
    
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
    public Render getRender() {
        return new RenderBoat();
    }
    
}
