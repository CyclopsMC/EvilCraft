package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.EntityConfig;
import evilcraft.api.render.RenderThrowable;
import evilcraft.items.WeatherContainer;

/**
 * Config for the {@link EntityWeatherContainer}.
 * @author rubensworks
 *
 */
public class EntityWeatherContainerConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityWeatherContainerConfig _instance;
    
    /**
     * Make a new instance.
     */
    public EntityWeatherContainerConfig() {
        super(
        	true,
            "entityWeatherContainer",
            null,
            EntityWeatherContainer.class
        );
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
        return new RenderThrowable(WeatherContainer.getInstance());
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
}
