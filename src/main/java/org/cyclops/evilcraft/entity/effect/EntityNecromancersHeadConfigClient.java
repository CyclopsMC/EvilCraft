package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 */
public class EntityNecromancersHeadConfigClient extends EntityClientConfig<IModBase, EntityNecromancersHead> {
    public EntityNecromancersHeadConfigClient(EntityConfigCommon<IModBase, EntityNecromancersHead> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityNecromancersHead, ?> getRender(EntityRendererProvider.Context renderContext) {
        return new ThrownItemRenderer<>(renderContext);
    }
}
