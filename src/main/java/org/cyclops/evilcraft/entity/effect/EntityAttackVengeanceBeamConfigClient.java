package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.core.client.render.RenderNull;

/**
 * @author rubensworks
 */
public class EntityAttackVengeanceBeamConfigClient extends EntityClientConfig<IModBase, EntityAttackVengeanceBeam> {
    public EntityAttackVengeanceBeamConfigClient(EntityConfigCommon<IModBase, EntityAttackVengeanceBeam> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityAttackVengeanceBeam, ?> getRender(EntityRendererProvider.Context renderContext) {
        return new RenderNull(renderContext);
    }
}
