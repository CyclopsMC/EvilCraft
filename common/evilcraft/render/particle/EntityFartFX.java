package evilcraft.render.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityFartFX extends EntityFX {
    
    public EntityFartFX(World world, double x, double y, double z, boolean rainbow) {
        super(world, x, y, z);
        
        setParticleSettings(rainbow);
    }

    public EntityFartFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ, boolean rainbow) {
        super(world, x, y, z, motionX, motionY, motionZ);
        
        setParticleSettings(rainbow);
    }
    
    private void setParticleSettings(boolean rainbow) {
        particleScale = 3;
        particleAlpha = 0.7F;
        
        if (!rainbow) {
            particleRed = 0.50F + rand.nextFloat() * 0.2F;
            particleGreen = 0.3F + rand.nextFloat() * 0.1F;
            particleBlue = rand.nextFloat() * 0.2F;
        } else {
            particleRed = rand.nextFloat();
            particleGreen = rand.nextFloat();
            particleBlue = rand.nextFloat();
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        
        particleScale = (1 - (float)particleAge / particleMaxAge) * 3;
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
    }
    
    @Override
    public int getFXLayer() {
        return 0;
    }

}
