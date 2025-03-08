package org.cyclops.evilcraft.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityItemUndespawnable}.
 * @author rubensworks
 *
 */
public class EntityItemUndespawnableConfig extends EntityConfigCommon<IModBase, EntityItemUndespawnable> {

    public EntityItemUndespawnableConfig() {
        super(
                EvilCraft._instance,
                "item_undespawnable",
                eConfig -> EntityType.Builder.<EntityItemUndespawnable>of(EntityItemUndespawnable::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityItemUndespawnable> constructEntityClientConfig() {
        return new EntityItemUndespawnableConfigClient(this);
    }
}
