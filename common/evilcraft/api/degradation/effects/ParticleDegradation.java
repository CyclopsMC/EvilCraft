package evilcraft.api.degradation.effects;

import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import evilcraft.api.Coordinate;
import evilcraft.api.degradation.IDegradable;
import evilcraft.api.degradation.IDegradationEffect;
import evilcraft.render.particle.EntityDegradeFX;

/**
 * An effect that will knockback the entities within the range of the degradable.
 * @author rubensworks
 *
 */
public class ParticleDegradation implements IDegradationEffect {

    @Override
    public boolean canRun(IDegradable degradable) {
        return true;
    }

    @Override
    public void runClientSide(IDegradable degradable) {
        Coordinate center = degradable.getLocation();
        World world = degradable.getWorld();
        int radius = degradable.getRadius();
        
        double xCoord = center.x - radius + 2 * radius * world.rand.nextFloat();
        double yCoord = center.y - radius + 2 * radius * world.rand.nextFloat();
        double zCoord = center.z - radius + 2 * radius * world.rand.nextFloat();
        
        double particleX = xCoord;
        double particleY = yCoord;
        double particleZ = zCoord;

        float particleMotionX = world.rand.nextFloat() * 1.4F - 0.7F;
        float particleMotionY = -0.2F;
        float particleMotionZ = world.rand.nextFloat() * 1.4F - 0.7F;
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                new EntityDegradeFX(world, particleX, particleY, particleZ,
                        particleMotionX, particleMotionY, particleMotionZ)
                );
    }

    @Override
    public void runServerSide(IDegradable degradable) {
        
    }
    
}
