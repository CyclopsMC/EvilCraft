package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityBloodPearl}.
 * @author rubensworks
 *
 */
public class EntityBloodPearlConfig extends EntityConfig<EntityBloodPearl> {

    public EntityBloodPearlConfig() {
        super(
                EvilCraft._instance,
                "blood_pearl",
                eConfig -> EntityType.Builder.<EntityBloodPearl>of(EntityBloodPearl::new, MobCategory.MISC)
                        .sized(0.6F, 1.8F)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityBloodPearl> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new ThrownItemRenderer<>(renderContext);
    }

}
