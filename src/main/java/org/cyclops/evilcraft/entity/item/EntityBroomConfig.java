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
import org.cyclops.evilcraft.client.render.entity.RenderBroom;

/**
 * Config for the {@link EntityBroom}.
 * @author rubensworks
 *
 */
public class EntityBroomConfig extends EntityConfig<EntityBroom> {

    public EntityBroomConfig() {
        super(
                EvilCraft._instance,
            "broom",
                eConfig -> EntityType.Builder.<EntityBroom>of(EntityBroom::new, MobCategory.MISC)
                        .sized(0.6F, 1.8F)
                        .setShouldReceiveVelocityUpdates(true)
                        .setUpdateInterval(10)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityBroom> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderBroom(renderContext, this);
    }
}
