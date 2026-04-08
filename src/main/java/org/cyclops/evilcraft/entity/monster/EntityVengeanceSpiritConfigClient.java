package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.client.render.entity.RenderVengeanceSpirit;

/**
 * @author rubensworks
 */
public class EntityVengeanceSpiritConfigClient extends EntityClientConfig<IModBase, EntityVengeanceSpirit> {
    public EntityVengeanceSpiritConfigClient(EntityConfigCommon<IModBase, EntityVengeanceSpirit> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityVengeanceSpirit, ?> getRender(EntityRendererProvider.Context renderContext) {
        return new RenderVengeanceSpirit(renderContext, (EntityVengeanceSpiritConfig) getEntityConfig());
    }
}
