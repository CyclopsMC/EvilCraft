package org.cyclops.evilcraft.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityBroom}.
 * @author rubensworks
 *
 */
public class EntityBroomConfig extends EntityConfigCommon<IModBase, EntityBroom> {

    public EntityBroomConfig() {
        super(
                EvilCraft._instance,
            "broom",
                eConfig -> EntityType.Builder.<EntityBroom>of(EntityBroom::new, MobCategory.MISC)
                        .sized(0.6F, 1.8F)
                        .setShouldReceiveVelocityUpdates(true)
                        .setUpdateInterval(10)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityBroom> constructEntityClientConfig() {
        return new EntityBroomConfigClient(this);
    }
}
