package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.core.client.render.RenderNull;

/**
 * @author rubensworks
 */
public class EntityAntiVengeanceBeamConfigClient extends EntityClientConfig<IModBase, EntityAntiVengeanceBeam> {
    public EntityAntiVengeanceBeamConfigClient(EntityConfigCommon<IModBase, EntityAntiVengeanceBeam> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityAntiVengeanceBeam, ?> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderNull(renderContext);
    }
}
