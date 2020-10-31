package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.client.render.RenderNull;

/**
 * Config for the {@link EntityAttackVengeanceBeam}.
 * @author rubensworks
 *
 */
public class EntityAttackVengeanceBeamConfig extends EntityConfig<EntityAttackVengeanceBeam> {

    @ConfigurableProperty(category = "entity", comment = "If crossed beams should cause explosions.", isCommandable = true)
    public static boolean crossBeamsExplosions = true;

    public EntityAttackVengeanceBeamConfig() {
        super(
                EvilCraft._instance,
                "attack_vengeance_beam",
                eConfig -> EntityType.Builder.<EntityAttackVengeanceBeam>create(EntityAttackVengeanceBeam::new, EntityClassification.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<Entity> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
    	return new RenderNull(renderManager);
    }
    
}
