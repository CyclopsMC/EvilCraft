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
public class EntityBiomeExtractConfig extends EntityConfig<EntityBiomeExtract> {

    public EntityBiomeExtractConfig() {
        super(
                EvilCraft._instance,
            "biome_extract",
                eConfig -> EntityType.Builder.<EntityBiomeExtract>of(EntityBiomeExtract::new, MobCategory.MISC)
                        .sized(0.6F, 1.8F)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityThrowable> getRender(EntityRendererProvider.Context context, ItemRenderer renderItem) {
        return new RenderThrowable(context);
    }
}
