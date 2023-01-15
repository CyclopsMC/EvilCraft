package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.client.render.RenderNull;

/**
 * Config for the {@link EntityAntiVengeanceBeam}.
 * @author rubensworks
 *
 */
public class EntityAntiVengeanceBeamConfig extends EntityConfig<EntityAntiVengeanceBeam> {

    public EntityAntiVengeanceBeamConfig() {
        super(
                EvilCraft._instance,
                "anti_vengeance_beam",
                eConfig -> EntityType.Builder.<EntityAntiVengeanceBeam>of(EntityAntiVengeanceBeam::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<Entity> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderNull(renderContext);
    }

}
