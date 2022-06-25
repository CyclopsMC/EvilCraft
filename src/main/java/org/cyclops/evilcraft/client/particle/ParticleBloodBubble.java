package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SplashParticle;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;

import javax.annotation.Nullable;


/**
 * A blood bubble FX.
 * @author rubensworks
 * @see SplashParticle
 */
@OnlyIn(Dist.CLIENT)
public class ParticleBloodBubble extends WaterDropParticle {

    public ParticleBloodBubble(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.gravity = 0.04F;
        if (ySpeedIn == 0.0D && (xSpeedIn != 0.0D || zSpeedIn != 0.0D)) {
            this.xd = xSpeedIn;
            this.yd = 0.1D;
            this.zd = zSpeedIn;
        }
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.yd += 0.002D;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.85D;
        this.yd *= 0.85D;
        this.zd *= 0.85D;
        if (this.lifetime-- <= 0) {
            this.remove();
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
    @OnlyIn(Dist.CLIENT)
    public static void randomDisplayTick(@Nullable BlockEntityWorking tile, Level world, BlockPos blockPos, RandomSource random, Direction rotatedDirection) {
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

                    Minecraft.getInstance().levelRenderer.addParticle(
                            RegistryEntries.PARTICLE_BLOOD_BUBBLE, false,
                            particleX, particleY, particleZ,
                            particlemotionX, particlemotionY, particlemotionZ);
                }
            }
        }
    }
}
