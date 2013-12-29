package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import cpw.mods.fml.client.registry.RenderingRegistry;
import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.items.LightningGrenade;

public class EntityBroomConfig extends ExtendedConfig {
    
    public static EntityBroomConfig _instance;

    public EntityBroomConfig() {
        super(
            1,
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
