package org.cyclops.evilcraft.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityItemEmpowerable}.
 * @author rubensworks
 *
 */
public class EntityItemEmpowerableConfig extends EntityConfigCommon<IModBase, EntityItemEmpowerable> {

    public EntityItemEmpowerableConfig() {
        super(
                EvilCraft._instance,
                "item_empowerable",
                eConfig -> EntityType.Builder.<EntityItemEmpowerable>of(EntityItemEmpowerable::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityItemEmpowerable> constructEntityClientConfig() {
        return new EntityItemEmpowerableConfigClient(this);
    }
}
