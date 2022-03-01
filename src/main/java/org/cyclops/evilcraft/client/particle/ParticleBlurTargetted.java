package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import org.cyclops.cyclopscore.client.particle.ParticleBlur;

/**
 * A blurred static fading particle with any possible color targetted at a certain location.
 * @author rubensworks
 *
 */
public class ParticleBlurTargetted extends ParticleBlur {

    private final double targetX;
    private final double targetY;
    private final double targetZ;

    public ParticleBlurTargetted(ParticleBlurTargettedData data, ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(data, world, x, y, z, motionX, motionY, motionZ);
        this.targetX = data.getTargetX();
        this.targetY = data.getTargetY();
        this.targetZ = data.getTargetZ();
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;

        if(age++ >= lifetime) {
            remove();
        }

        float f = (float)this.age / (float)this.lifetime;
        float f1 = f;
        f = -f + f * f * 2.0F;
        f = 1.0F - f;
        yd -= 0.04D * gravity;
        x = targetX + xd * f;
        y = targetY + yd * f + (double)(1.0F - f1);
        z = targetZ + zd * f;
    }

}
