package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 */
public class EntityRedstoneGrenadeConfigClient extends EntityClientConfig<IModBase, EntityRedstoneGrenade> {
    public EntityRedstoneGrenadeConfigClient(EntityConfigCommon<IModBase, EntityRedstoneGrenade> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityRedstoneGrenade, ?> getRender(EntityRendererProvider.Context renderContext) {
        return new ThrownItemRenderer<>(renderContext);
    }
}
