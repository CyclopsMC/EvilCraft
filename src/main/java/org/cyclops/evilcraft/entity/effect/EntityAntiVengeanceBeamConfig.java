package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
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
                eConfig -> EntityType.Builder.<EntityAntiVengeanceBeam>of(EntityAntiVengeanceBeam::new, EntityClassification.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<Entity> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
    	return new RenderNull(renderManager);
    }
    
}
