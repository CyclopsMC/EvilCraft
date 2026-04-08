package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.client.render.entity.RenderControlledZombie;

/**
 * @author rubensworks
 */
public class EntityControlledZombieConfigClient extends EntityClientConfig<IModBase, EntityControlledZombie> {
    public EntityControlledZombieConfigClient(EntityConfigCommon<IModBase, EntityControlledZombie> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityControlledZombie, ?> getRender(EntityRendererProvider.Context renderContext) {
        return new RenderControlledZombie((EntityControlledZombieConfig) getEntityConfig(), renderContext);
    }
}
