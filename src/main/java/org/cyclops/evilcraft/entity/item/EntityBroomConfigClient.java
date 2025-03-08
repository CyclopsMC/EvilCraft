package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.client.render.entity.RenderBroom;

/**
 * @author rubensworks
 */
public class EntityBroomConfigClient extends EntityClientConfig<IModBase, EntityBroom> {
    public EntityBroomConfigClient(EntityConfigCommon<IModBase, EntityBroom> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityBroom, ?> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderBroom(renderContext);
    }
}
