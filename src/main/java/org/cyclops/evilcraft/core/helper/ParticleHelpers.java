package org.cyclops.evilcraft.core.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleHelpers {
    public static void spawnBloodSplashParticles(Level level, BlockPos blockPos, int velocity, int amount) {
        RandomSource random = level.getRandom();
        for (int i = 0; i < amount; i++) {
            float x_r = blockPos.getX() + random.nextFloat();
            float y_r = blockPos.getY() + random.nextFloat();
            float z_r = blockPos.getZ() + random.nextFloat();

            level.addParticle(
                    RegistryEntries.PARTICLE_BLOOD_SPLASH.get(),
                    x_r, y_r, z_r,
                    velocity == 0 ? 0 : random.nextInt(velocity),
                    velocity == 0 ? 0 : random.nextInt(velocity),
                    velocity == 0 ? 0 : random.nextInt(velocity));
        }
    }

    /**
     * Call this in machines that should display blood particles when working.
     * @param tile The tile working entity.
     * @param world The world.
     * @param blockPos The blockState position.
     * @param random Random instance.
     * @param rotatedDirection The direction to emit the particles at.
     */
    public static void spawnRandomBloodBubbleParticles(@Nullable BlockEntityWorking tile, Level world, BlockPos blockPos, RandomSource random, Direction rotatedDirection) {
        if(tile != null && random.nextInt(10) == 0) {
            if (tile.isVisuallyWorking()) {
                for(int i = 0; i < 1 + random.nextInt(5); i++) {
                    double particleX = blockPos.getX() - rotatedDirection.getStepX() + (rotatedDirection == Direction.EAST ? 1 : 0)
                            + (rotatedDirection == Direction.NORTH || rotatedDirection == Direction.SOUTH ?
                            (0.3 + random.nextDouble() * 0.4) : 0);
                    double particleY = blockPos.getY() + 0.1 + random.nextDouble() * 0.5;
                    double particleZ = blockPos.getZ() - rotatedDirection.getStepZ() + (rotatedDirection == Direction.SOUTH ? 1 : 0)
                            + (rotatedDirection == Direction.EAST || rotatedDirection == Direction.WEST ?
                            (0.3 + random.nextDouble() * 0.4) : 0);

                    float particlemotionX = -0.1F + random.nextFloat() * 0.2F;
                    float particlemotionY = 0.01F;
                    float particlemotionZ = -0.1F + random.nextFloat() * 0.2F;

                    world.addParticle(
                            RegistryEntries.PARTICLE_BLOOD_BUBBLE.get(),
                            particleX, particleY, particleZ,
                            particlemotionX, particlemotionY, particlemotionZ);
                }
            }
        }
    }
}
