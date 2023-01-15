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
 * Config for {@link EntityLightningGrenade}.
 * @author rubensworks
 *
 */
public class EntityLightningGrenadeConfig extends EntityConfig<EntityLightningGrenade> {

    public EntityLightningGrenadeConfig() {
        super(
                EvilCraft._instance,
            "lightning_grenade",
                eConfig -> EntityType.Builder.<EntityLightningGrenade>of(EntityLightningGrenade::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityLightningGrenade> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new ThrownItemRenderer<>(renderContext);
    }
}
