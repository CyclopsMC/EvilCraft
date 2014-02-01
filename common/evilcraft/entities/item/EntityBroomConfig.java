package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.Render;
import evilcraft.Reference;
import evilcraft.api.config.EntityConfig;
import evilcraft.render.entity.RenderBroom;

public class EntityBroomConfig extends EntityConfig {
    
    public static EntityBroomConfig _instance;

    public EntityBroomConfig() {
        super(
            Reference.ENTITY_BROOM,
            "Broom",
            "broom",
            null,
            EntityBroom.class
        );
    }
    
    @Override
    protected Render getRender() {
        return new RenderBroom(this);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
}
