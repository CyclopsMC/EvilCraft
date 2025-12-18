package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SplashParticle;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;


/**
 * A blood splashing FX.
 * @author rubensworks
 * @see SplashParticle
 */
public class ParticleBloodSplash extends WaterDropParticle {

    public ParticleBloodSplash(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, TextureAtlasSprite sprite) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, sprite);
        this.gravity = 0.04F;
        if (ySpeedIn == 0.0D && (xSpeedIn != 0.0D || zSpeedIn != 0.0D)) {
            this.xd = xSpeedIn;
            this.yd = 0.1D;
            this.zd = zSpeedIn;
        }
    }

}
