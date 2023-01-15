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
import org.cyclops.evilcraft.client.render.entity.RenderDarkStick;

/**
 * Config for the {@link EntityItemDarkStick}.
 * @author rubensworks
 *
 */
public class EntityItemDarkStickConfig extends EntityConfig<EntityItemDarkStick> {

    public EntityItemDarkStickConfig() {
        super(
                EvilCraft._instance,
                "item_dark_stick",
                eConfig -> EntityType.Builder.<EntityItemDarkStick>of(EntityItemDarkStick::new, MobCategory.MISC)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityRenderer<EntityItemDarkStick> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderDarkStick(renderContext, this);
    }
}
