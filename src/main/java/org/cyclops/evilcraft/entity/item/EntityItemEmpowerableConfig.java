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
 * Config for the {@link EntityItemEmpowerable}.
 * @author rubensworks
 *
 */
public class EntityItemEmpowerableConfig extends EntityConfig<EntityItemEmpowerable> {

    public EntityItemEmpowerableConfig() {
        super(
                EvilCraft._instance,
                "item_empowerable",
                eConfig -> EntityType.Builder.<EntityItemEmpowerable>of(EntityItemEmpowerable::new, EntityClassification.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @OnlyIn(Dist.CLIENT)
	@Override
	public EntityRenderer<ItemEntity> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
		return new net.minecraft.client.renderer.entity.ItemRenderer(renderManager, renderItem);
	}
}
