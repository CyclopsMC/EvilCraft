package org.cyclops.evilcraft.entity.effect;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityAttackVengeanceBeam}.
 * @author rubensworks
 *
 */
public class EntityAttackVengeanceBeamConfig extends EntityConfigCommon<IModBase, EntityAttackVengeanceBeam> {

    @ConfigurablePropertyCommon(category = "entity", comment = "If crossed beams should cause explosions.", isCommandable = true)
    public static boolean crossBeamsExplosions = true;

    public EntityAttackVengeanceBeamConfig() {
        super(
                EvilCraft._instance,
                "attack_vengeance_beam",
                eConfig -> EntityType.Builder.<EntityAttackVengeanceBeam>of(EntityAttackVengeanceBeam::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityAttackVengeanceBeam> constructEntityClientConfig() {
        return new EntityAttackVengeanceBeamConfigClient(this);
    }
}
