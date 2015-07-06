package evilcraft.core.degradation.effect;

import evilcraft.api.degradation.IDegradable;
import evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import evilcraft.core.degradation.StochasticDegradationEffect;
import evilcraft.core.helper.EntityHelpers;
import evilcraft.core.helper.LocationHelpers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * Degradation that will eventually spawn mobs in the area.
 * @author rubensworks
 *
 */
public class MobSpawnDegradation extends StochasticDegradationEffect {

    private static MobSpawnDegradation _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<DegradationEffectConfig> eConfig) {
        if(_instance == null)
            _instance = new MobSpawnDegradation(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static MobSpawnDegradation getInstance() {
        return _instance;
    }
    
    private static final double CHANCE = 0.01D;
    
    private MobSpawnDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
        super(eConfig, CHANCE);
    }
    
    @Override
    public void runClientSide(IDegradable degradable) {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void runServerSide(IDegradable degradable) {
        WorldServer world = (WorldServer) degradable.getWorld();
        BlockPos spawn = LocationHelpers.getRandomPointInSphere(degradable.getLocation(), degradable.getRadius());
        float x = spawn.getX() + 0.5F;
        float y = spawn.getY();
        float z = spawn.getZ() + 0.5F;
        SpawnListEntry spawnlistentry = world.getSpawnListEntryForTypeAt(EnumCreatureType.MONSTER, spawn);
        EntityLiving entityliving;

        try {
            entityliving = (EntityLiving)spawnlistentry.entityClass.getConstructor(World.class).newInstance(world);
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        entityliving.setLocationAndAngles((double)x, (double)y, (double)z, world.rand.nextFloat() * 360.0F, 0.0F);
        EntityHelpers.spawnEntity(world, entityliving);
    }

}
