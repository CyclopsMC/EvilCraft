package org.cyclops.evilcraft.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * {@link EntityBubbleFX} that has a modifiable gravity factor.
 * The higher this factor, the more quickly it will drop.
 * @author Ruben Taelman
 */
@SideOnly(Side.CLIENT)
public class ExtendedEntityBubbleFX extends EntityBubbleFX {

    private final double gravity;

    public ExtendedEntityBubbleFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ, double gravity) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.gravity = gravity;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= gravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.85D;
        this.motionY *= 0.85D;
        this.motionZ *= 0.85D;

        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
    }
}
