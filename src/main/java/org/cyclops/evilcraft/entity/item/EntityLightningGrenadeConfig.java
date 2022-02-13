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
 * Config for {@link EntityLightningGrenade}.
 * @author rubensworks
 *
 */
public class EntityLightningGrenadeConfig extends EntityConfig<EntityLightningGrenade> {

    public EntityLightningGrenadeConfig() {
        super(
                EvilCraft._instance,
            "lightning_grenade",
                eConfig -> EntityType.Builder.<EntityLightningGrenade>of(EntityLightningGrenade::new, EntityClassification.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityLightningGrenade> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
        return new SpriteRenderer<>(renderManager, renderItem);
    }
}
