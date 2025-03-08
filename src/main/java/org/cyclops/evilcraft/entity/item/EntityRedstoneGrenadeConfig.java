package org.cyclops.evilcraft.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityRedstoneGrenade}.
 * @author rubensworks
 *
 */
public class EntityRedstoneGrenadeConfig extends EntityConfigCommon<IModBase, EntityRedstoneGrenade> {

    public EntityRedstoneGrenadeConfig() {
        super(
                EvilCraft._instance,
            "redstone_grenade",
                eConfig -> EntityType.Builder.<EntityRedstoneGrenade>of(EntityRedstoneGrenade::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityRedstoneGrenade> constructEntityClientConfig() {
        return new EntityRedstoneGrenadeConfigClient(this);
    }
}
