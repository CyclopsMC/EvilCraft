package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.Render;
import evilcraft.Reference;
import evilcraft.api.config.ModelEntityConfig;
import evilcraft.api.render.ModelRender;
import evilcraft.render.entity.RenderBroom;

public class EntityBroomConfig extends ModelEntityConfig {
    
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
    public Class<? extends ModelRender> getRenderClass() {
        return RenderBroom.class;
    }
    
}
