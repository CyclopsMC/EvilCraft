package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityItemUndespawnable}.
 * @author rubensworks
 *
 */
public class EntityItemUndespawnableConfig extends EntityConfig<EntityItemUndespawnable> {

    public EntityItemUndespawnableConfig() {
        super(
                EvilCraft._instance,
                "item_undespawnable",
                eConfig -> EntityType.Builder.<EntityItemUndespawnable>create(EntityItemUndespawnable::new, EntityClassification.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
	@Override
	public EntityRenderer<ItemEntity> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
        return new net.minecraft.client.renderer.entity.ItemRenderer(renderManager, renderItem);
	}
}
