package evilcraft.entities.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
    
    @SideOnly(Side.CLIENT)
    @Override
    protected Render getRender() {
        return new RenderBroom(this);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
    @Override
    public int getUpdateFrequency() {
        return 5;
    }
}
