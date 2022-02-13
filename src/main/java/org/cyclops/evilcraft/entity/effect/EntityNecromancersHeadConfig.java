package org.cyclops.evilcraft.entity.effect;

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
 * Config for the {@link EntityNecromancersHead}.
 * @author rubensworks
 *
 */
public class EntityNecromancersHeadConfig extends EntityConfig<EntityNecromancersHead> {

    public EntityNecromancersHeadConfig() {
        super(
                EvilCraft._instance,
                "necromancers_head",
                eConfig -> EntityType.Builder.<EntityNecromancersHead>of(EntityNecromancersHead::new, EntityClassification.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityNecromancersHead> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
        return new SpriteRenderer<>(renderManager, renderItem);
    }
    
}
