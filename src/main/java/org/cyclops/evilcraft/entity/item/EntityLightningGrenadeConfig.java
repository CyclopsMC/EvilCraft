package org.cyclops.evilcraft.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EntityLightningGrenade}.
 * @author rubensworks
 *
 */
public class EntityLightningGrenadeConfig extends EntityConfigCommon<IModBase, EntityLightningGrenade> {

    public EntityLightningGrenadeConfig() {
        super(
                EvilCraft._instance,
            "lightning_grenade",
                eConfig -> EntityType.Builder.<EntityLightningGrenade>of(EntityLightningGrenade::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityLightningGrenade> constructEntityClientConfig() {
        return new EntityLightningGrenadeConfigClient(this);
    }
}
