package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SplashParticle;
import net.minecraft.client.particle.WaterDropParticle;


/**
 * A blood bubble FX.
 * @author rubensworks
 * @see SplashParticle
 */
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

}
