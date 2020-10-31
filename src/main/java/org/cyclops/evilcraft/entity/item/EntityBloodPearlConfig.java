package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
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
                eConfig -> EntityType.Builder.<EntityBloodPearl>create(EntityBloodPearl::new, EntityClassification.MISC)
                        .size(0.6F, 1.8F)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityBloodPearl> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
        return new SpriteRenderer<>(renderManager, renderItem);
    }
    
}
