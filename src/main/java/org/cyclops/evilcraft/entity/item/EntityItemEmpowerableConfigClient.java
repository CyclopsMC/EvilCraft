package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 */
public class EntityItemEmpowerableConfigClient extends EntityClientConfig<IModBase, EntityItemEmpowerable> {
    public EntityItemEmpowerableConfigClient(EntityConfigCommon<IModBase, EntityItemEmpowerable> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityItemEmpowerable, ?> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new net.minecraft.client.renderer.entity.ItemEntityRenderer(renderContext);
    }
}
