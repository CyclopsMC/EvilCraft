package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.client.render.RenderThrowable;
import org.cyclops.evilcraft.item.WeatherContainer;

/**
 * Config for the {@link EntityWeatherContainer}.
 * @author rubensworks
 *
 */
public class EntityWeatherContainerConfig extends EntityConfig<EntityWeatherContainer> {
    
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
        return new RenderThrowable(renderManager, WeatherContainer.getInstance(), Minecraft.getMinecraft().getRenderItem());
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
}
