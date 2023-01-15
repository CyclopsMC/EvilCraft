package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

import java.util.List;

/**
 * An effect that will knockback the entities within the range of the degradable.
 * @author rubensworks
 *
 */
public class KnockbackDistortDegradation implements IDegradationEffect {

    private static final int MINIMUM_DEGRADATION = 3;
    private static final int POWER_LEVEL = 1;

    public KnockbackDistortDegradation(DegradationEffectConfig eConfig) {

    }

    @Override
    public boolean canRun(IDegradable degradable) {
        return degradable.getDegradation() >= MINIMUM_DEGRADATION;
    }

    @Override
    public void runClientSide(IDegradable degradable) {
        runServerSide(degradable);
    }

    @Override
    public void runServerSide(IDegradable degradable) {
        List<Entity> entities = degradable.getAreaEntities();
        BlockPos center = degradable.getLocation();
        double x = center.getX();
        double y = center.getY();
        double z = center.getZ();
        for(Entity entity : entities) {
            RegistryEntries.ITEM_MACE_OF_DISTORTION.distortEntity(
                    degradable.getDegradationWorld(),
                    null,
                    entity,
                    x, y, z,
                    (int) degradable.getDegradation() * 10,
                    POWER_LEVEL
            );
        }
    }

}
