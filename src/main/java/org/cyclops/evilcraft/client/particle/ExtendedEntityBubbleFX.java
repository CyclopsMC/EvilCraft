package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * {@link EntityBubbleFX} that has a modifiable gravity factor.
 * The higher this factor, the more quickly it will drop.
 * @author Ruben Taelman
 */
@SideOnly(Side.CLIENT)
public class ExtendedEntityBubbleFX extends EntityBubbleFX {

    private final double gravity;

    public ExtendedEntityBubbleFX(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, double gravity) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.gravity = gravity;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.ySpeed -= gravity;
        this.moveEntity(this.xSpeed, this.ySpeed, this.zSpeed);
        this.xSpeed *= 0.85D;
        this.ySpeed *= 0.85D;
        this.zSpeed *= 0.85D;

        if (this.particleMaxAge-- <= 0) {
            this.setExpired();
        }
    }
}
