package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.client.render.RenderThrowable;
import org.cyclops.evilcraft.core.entity.item.EntityThrowable;

/**
 * Config for the {@link EntityWeatherContainer}.
 * @author rubensworks
 *
 */
public class EntityWeatherContainerConfig extends EntityConfig<EntityWeatherContainer> {

    public EntityWeatherContainerConfig() {
        super(
                EvilCraft._instance,
            "weather_container",
                eConfig -> EntityType.Builder.<EntityWeatherContainer>create(EntityWeatherContainer::new, EntityClassification.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityThrowable> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
        return new RenderThrowable(renderManager, renderItem);
    }

}
