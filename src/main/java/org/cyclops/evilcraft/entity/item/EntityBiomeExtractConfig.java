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
public class EntityBiomeExtractConfig extends EntityConfig<EntityBiomeExtract> {

    public EntityBiomeExtractConfig() {
        super(
                EvilCraft._instance,
            "biome_extract",
                eConfig -> EntityType.Builder.<EntityBiomeExtract>of(EntityBiomeExtract::new, EntityClassification.MISC)
                        .sized(0.6F, 1.8F)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityThrowable> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
        return new RenderThrowable(renderManager, renderItem);
    }
}
