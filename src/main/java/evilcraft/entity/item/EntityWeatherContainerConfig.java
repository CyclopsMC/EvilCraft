package evilcraft.entity.item;

import evilcraft.core.client.render.RenderThrowable;
import evilcraft.core.config.extendedconfig.EntityConfig;
import evilcraft.item.WeatherContainer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public Render getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderThrowable(WeatherContainer.getInstance());
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
}
