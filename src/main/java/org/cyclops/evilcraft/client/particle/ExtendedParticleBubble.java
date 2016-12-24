package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * {@link ParticleBubble} that has a modifiable gravity factor.
 * The higher this factor, the more quickly it will drop.
 * @author Ruben Taelman
 */
@SideOnly(Side.CLIENT)
public class ExtendedParticleBubble extends ParticleBubble {

    private final double gravity;

    public ExtendedParticleBubble(World world, double x, double y, double z, double motionX, double motionY, double motionZ, double gravity) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.gravity = gravity;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= gravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.85D;
        this.motionY *= 0.85D;
        this.motionZ *= 0.85D;

        if (this.particleMaxAge-- <= 0) {
            this.setExpired();
        }
    }
}
