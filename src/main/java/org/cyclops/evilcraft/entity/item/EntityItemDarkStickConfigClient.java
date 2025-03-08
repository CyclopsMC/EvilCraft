package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.client.render.entity.RenderDarkStick;

/**
 * @author rubensworks
 */
public class EntityItemDarkStickConfigClient extends EntityClientConfig<IModBase, EntityItemDarkStick> {
    public EntityItemDarkStickConfigClient(EntityConfigCommon<IModBase, EntityItemDarkStick> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityItemDarkStick, ?> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderDarkStick(renderContext);
    }
}
