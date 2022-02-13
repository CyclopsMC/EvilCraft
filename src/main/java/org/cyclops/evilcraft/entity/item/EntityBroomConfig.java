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
                eConfig -> EntityType.Builder.<EntityBroom>of(EntityBroom::new, EntityClassification.MISC)
                        .sized(0.6F, 1.8F)
                        .setShouldReceiveVelocityUpdates(true)
                        .setUpdateInterval(10)
        );
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityBroom> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
        return new RenderBroom(renderManager, this);
    }
}
