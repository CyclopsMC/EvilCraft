package evilcraft.render.particle;

import evilcraft.items.BloodPearlOfTeleportation;
import evilcraft.items.MaceOfDistortion;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

/**
 * An effect for the distortion radial effect.
 * @author rubensworks
 *
 */
public class EntityDistortFX extends EntityFX {

    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param motionX X axis speed.
     * @param motionY Y axis speed.
     * @param motionZ Z axis speed.
     * @param scale Scale of the particle.
     */
    public EntityDistortFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {
        super(world, x, y, z, motionX, motionY, motionZ);
        
        setParticleIcon(BloodPearlOfTeleportation.getInstance().getIconFromDamage(0));
        
        particleScale = scale;
        particleAlpha = 0.1F;
        particleMaxAge = MaceOfDistortion.AOE_TICK_UPDATE;
        
        particleRed = 0.001F * rand.nextFloat();
        particleGreen = 0.001F * rand.nextFloat();
        particleBlue = 0.001F * rand.nextFloat();
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }

}
