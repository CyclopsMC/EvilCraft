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
 * Config for the {@link EntityRedstoneGrenade}.
 * @author rubensworks
 *
 */
public class EntityRedstoneGrenadeConfig extends EntityConfig<EntityRedstoneGrenade> {

    public EntityRedstoneGrenadeConfig() {
        super(
                EvilCraft._instance,
            "redstone_grenade",
                eConfig -> EntityType.Builder.<EntityRedstoneGrenade>of(EntityRedstoneGrenade::new, EntityClassification.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<EntityRedstoneGrenade> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
        return new SpriteRenderer<>(renderManager, renderItem);
    }
    
}
