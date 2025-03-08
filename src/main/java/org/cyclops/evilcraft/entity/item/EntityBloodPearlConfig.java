package org.cyclops.evilcraft.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityBloodPearl}.
 * @author rubensworks
 *
 */
public class EntityBloodPearlConfig extends EntityConfigCommon<IModBase, EntityBloodPearl> {

    public EntityBloodPearlConfig() {
        super(
                EvilCraft._instance,
                "blood_pearl",
                eConfig -> EntityType.Builder.<EntityBloodPearl>of(EntityBloodPearl::new, MobCategory.MISC)
                        .sized(0.6F, 1.8F)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityBloodPearl> constructEntityClientConfig() {
        return new EntityBloodPearlConfigClient(this);
    }
}
