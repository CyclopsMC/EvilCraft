package org.cyclops.evilcraft.entity.effect;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityAntiVengeanceBeam}.
 * @author rubensworks
 *
 */
public class EntityAntiVengeanceBeamConfig extends EntityConfigCommon<IModBase, EntityAntiVengeanceBeam> {

    public EntityAntiVengeanceBeamConfig() {
        super(
                EvilCraft._instance,
                "anti_vengeance_beam",
                eConfig -> EntityType.Builder.<EntityAntiVengeanceBeam>of(EntityAntiVengeanceBeam::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityAntiVengeanceBeam> constructEntityClientConfig() {
        return new EntityAntiVengeanceBeamConfigClient(this);
    }
}
