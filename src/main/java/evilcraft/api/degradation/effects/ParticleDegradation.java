package evilcraft.api.degradation.effects;

import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Coordinate;
import evilcraft.api.config.DegradationEffectConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableDegradationEffect;
import evilcraft.api.degradation.IDegradable;
import evilcraft.render.particle.EntityDegradeFX;

/**
 * An effect that will knockback the entities within the range of the degradable.
 * @author rubensworks
 *
 */
public class ParticleDegradation extends ConfigurableDegradationEffect {

    private static ParticleDegradation _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<DegradationEffectConfig> eConfig) {
        if(_instance == null)
            _instance = new ParticleDegradation(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ParticleDegradation getInstance() {
        return _instance;
    }
    
    private ParticleDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
        super(eConfig);
    }
    
    @Override
    public boolean canRun(IDegradable degradable) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
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
