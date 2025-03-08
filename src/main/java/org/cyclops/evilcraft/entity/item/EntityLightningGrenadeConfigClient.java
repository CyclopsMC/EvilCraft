package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 */
public class EntityLightningGrenadeConfigClient extends EntityClientConfig<IModBase, EntityLightningGrenade> {
    public EntityLightningGrenadeConfigClient(EntityConfigCommon<IModBase, EntityLightningGrenade> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityLightningGrenade, ?> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new ThrownItemRenderer<>(renderContext);
    }
}
