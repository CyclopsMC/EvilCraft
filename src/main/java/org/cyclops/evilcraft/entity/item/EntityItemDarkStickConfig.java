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
                eConfig -> EntityType.Builder.<EntityItemDarkStick>create(EntityItemDarkStick::new, EntityClassification.MISC)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityRenderer<EntityItemDarkStick> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
        return new RenderDarkStick(renderManager, this);
    }
}
