package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.Render;
import evilcraft.Reference;
import evilcraft.api.config.EntityConfig;
import evilcraft.api.render.RenderThrowable;
import evilcraft.items.WeatherContainer;

public class EntityWeatherContainerConfig extends EntityConfig {
    public static EntityWeatherContainerConfig _instance;
    
    public EntityWeatherContainerConfig() {
        super(
            Reference.ENTITY_WEATHERCONTAINER,
            "Weather Container",
            "entityWeatherContainer",
            null,
            EntityWeatherContainer.class
        );
    }
    
    @Override
    public Render getRender() {
        return new RenderThrowable(WeatherContainer.getInstance());
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
}
