package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * An effect that will knockback the entities within the range of the degradable.
 * @author rubensworks
 *
 */
public class ParticleDegradation implements IDegradationEffect {

    public ParticleDegradation(DegradationEffectConfig eConfig) {

    }

    @Override
    public boolean canRun(IDegradable degradable) {
        return true;
    }

    @Override
    public void runClientSide(IDegradable degradable) {
        BlockPos center = degradable.getLocation();
        Level world = degradable.getDegradationWorld();
        int radius = degradable.getRadius();

        double xCoord = center.getX() - radius + 2 * radius * world.getRandom().nextFloat();
        double yCoord = center.getY() - radius + 2 * radius * world.getRandom().nextFloat();
        double zCoord = center.getZ() - radius + 2 * radius * world.getRandom().nextFloat();

        double particleX = xCoord;
        double particleY = yCoord;
        double particleZ = zCoord;

        float particleMotionX = world.getRandom().nextFloat() * 1.4F - 0.7F;
        float particleMotionY = -0.2F;
        float particleMotionZ = world.getRandom().nextFloat() * 1.4F - 0.7F;
        world.addParticle(
                RegistryEntries.PARTICLE_DEGRADE.get(),
                particleX, particleY, particleZ,
                particleMotionX, particleMotionY, particleMotionZ);
    }

    @Override
    public void runServerSide(IDegradable degradable) {

    }

}
