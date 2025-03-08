package org.cyclops.evilcraft.entity.block;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.block.RenderBombPrimed;

/**
 * @author rubensworks
 */
public class EntityLightningBombPrimedConfigClient extends EntityClientConfig<IModBase, EntityLightningBombPrimed> {
    public EntityLightningBombPrimedConfigClient(EntityConfigCommon<IModBase, EntityLightningBombPrimed> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityLightningBombPrimed, ?> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderBombPrimed(renderContext, RegistryEntries.BLOCK_LIGHTNING_BOMB_PRIMED.get());
    }
}
