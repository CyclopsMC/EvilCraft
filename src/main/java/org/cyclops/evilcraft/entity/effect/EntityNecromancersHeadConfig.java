package org.cyclops.evilcraft.entity.effect;

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
 * Config for the {@link EntityNecromancersHead}.
 * @author rubensworks
 *
 */
public class EntityNecromancersHeadConfig extends EntityConfig<EntityNecromancersHead> {

    public EntityNecromancersHeadConfig() {
        super(
                EvilCraft._instance,
                "necromancers_head",
                eConfig -> EntityType.Builder.<EntityNecromancersHead>of(EntityNecromancersHead::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityNecromancersHead> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new ThrownItemRenderer<>(renderContext);
    }
}
