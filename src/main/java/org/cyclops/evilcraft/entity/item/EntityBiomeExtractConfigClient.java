package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.core.client.render.RenderThrowable;

/**
 * @author rubensworks
 */
public class EntityBiomeExtractConfigClient extends EntityClientConfig<IModBase, EntityBiomeExtract> {
    public EntityBiomeExtractConfigClient(EntityConfigCommon<IModBase, EntityBiomeExtract> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityBiomeExtract, ?> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderThrowable(renderContext);
    }
}
