package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
    @OnlyIn(Dist.CLIENT)
    public void runClientSide(IDegradable degradable) {
        BlockPos center = degradable.getLocation();
        Level world = degradable.getDegradationWorld();
        int radius = degradable.getRadius();

        double xCoord = center.getX() - radius + 2 * radius * world.random.nextFloat();
        double yCoord = center.getY() - radius + 2 * radius * world.random.nextFloat();
        double zCoord = center.getZ() - radius + 2 * radius * world.random.nextFloat();

        double particleX = xCoord;
        double particleY = yCoord;
        double particleZ = zCoord;

        float particleMotionX = world.random.nextFloat() * 1.4F - 0.7F;
        float particleMotionY = -0.2F;
        float particleMotionZ = world.random.nextFloat() * 1.4F - 0.7F;
        Minecraft.getInstance().levelRenderer.addParticle(
                RegistryEntries.PARTICLE_DEGRADE, false,
                particleX, particleY, particleZ,
                particleMotionX, particleMotionY, particleMotionZ);
    }

    @Override
    public void runServerSide(IDegradable degradable) {

    }

}
