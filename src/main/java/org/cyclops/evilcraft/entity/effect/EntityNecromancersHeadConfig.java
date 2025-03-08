package org.cyclops.evilcraft.entity.effect;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityNecromancersHead}.
 * @author rubensworks
 *
 */
public class EntityNecromancersHeadConfig extends EntityConfigCommon<IModBase, EntityNecromancersHead> {

    public EntityNecromancersHeadConfig() {
        super(
                EvilCraft._instance,
                "necromancers_head",
                eConfig -> EntityType.Builder.<EntityNecromancersHead>of(EntityNecromancersHead::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityNecromancersHead> constructEntityClientConfig() {
        return new EntityNecromancersHeadConfigClient(this);
    }
}
