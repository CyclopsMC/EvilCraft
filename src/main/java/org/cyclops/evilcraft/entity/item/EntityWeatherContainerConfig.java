package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
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
                eConfig -> EntityType.Builder.<EntityWeatherContainer>of(EntityWeatherContainer::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityThrowable> getRender(EntityRendererProvider.Context context, ItemRenderer renderItem) {
        return new RenderThrowable(context);
    }

}
