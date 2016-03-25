package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.client.particle.EntityDegradeFX;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableDegradationEffect;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * An effect that will knockback the entities within the range of the degradable.
 * @author rubensworks
 *
 */
public class ParticleDegradation extends ConfigurableDegradationEffect {

    private static ParticleDegradation _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ParticleDegradation getInstance() {
        return _instance;
    }
    
    public ParticleDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
        super(eConfig);
    }
    
    @Override
    public boolean canRun(IDegradable degradable) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void runClientSide(IDegradable degradable) {
        BlockPos center = degradable.getLocation();
        World world = degradable.getDegradationWorld();
        int radius = degradable.getRadius();
        
        double xCoord = center.getX() - radius + 2 * radius * world.rand.nextFloat();
        double yCoord = center.getY() - radius + 2 * radius * world.rand.nextFloat();
        double zCoord = center.getZ() - radius + 2 * radius * world.rand.nextFloat();
        
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
