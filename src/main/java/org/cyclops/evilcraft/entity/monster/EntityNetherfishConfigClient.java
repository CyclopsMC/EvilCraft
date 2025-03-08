package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.client.render.entity.RenderNetherfish;

/**
 * @author rubensworks
 */
public class EntityNetherfishConfigClient extends EntityClientConfig<IModBase, EntityNetherfish> {
    public EntityNetherfishConfigClient(EntityConfigCommon<IModBase, EntityNetherfish> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityNetherfish, ?> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderNetherfish(renderContext, (EntityNetherfishConfig) getEntityConfig());
    }
}
