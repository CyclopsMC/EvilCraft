package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.core.client.render.RenderThrowable;

/**
 * @author rubensworks
 */
public class EntityWeatherContainerConfigClient extends EntityClientConfig<IModBase, EntityWeatherContainer> {
    public EntityWeatherContainerConfigClient(EntityConfigCommon<IModBase, EntityWeatherContainer> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityWeatherContainer, ?> getRender(EntityRendererProvider.Context renderContext) {
        return new RenderThrowable(renderContext);
    }
}
