package evilcraft.entity.item;

import evilcraft.EvilCraft;
import evilcraft.core.client.render.RenderThrowable;
import evilcraft.item.WeatherContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;

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
                EvilCraft._instance,
        	true,
            "entityWeatherContainer",
            null,
            EntityWeatherContainer.class
        );
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderThrowable(Minecraft.getMinecraft().getRenderManager(), WeatherContainer.getInstance(), Minecraft.getMinecraft().getRenderItem());
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
}
