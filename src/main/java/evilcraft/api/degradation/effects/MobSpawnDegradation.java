package evilcraft.api.degradation.effects;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.event.ForgeEventFactory;
import cpw.mods.fml.common.eventhandler.Event.Result;
import evilcraft.api.algorithms.ILocation;
import evilcraft.api.config.DegradationEffectConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.degradation.IDegradable;
import evilcraft.api.degradation.StochasticDegradationEffect;
import evilcraft.api.helpers.LocationHelpers;

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
        ILocation spawn = LocationHelpers.getRandomPointInSphere(degradable.getLocation(), degradable.getRadius());
        float x = spawn.getCoordinates()[0] + 0.5F;
        float y = spawn.getCoordinates()[1];
        float z = spawn.getCoordinates()[2] + 0.5F;
        SpawnListEntry spawnlistentry = world.spawnRandomCreature(EnumCreatureType.monster,
        		spawn.getCoordinates()[0], spawn.getCoordinates()[1], spawn.getCoordinates()[2]);
        EntityLiving entityliving;

        try {
            entityliving = (EntityLiving)spawnlistentry.entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {world});
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        entityliving.setLocationAndAngles((double)x, (double)y, (double)z, world.rand.nextFloat() * 360.0F, 0.0F);

        Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, world, x, y, z);
        if (canSpawn == Result.ALLOW || (canSpawn == Result.DEFAULT)) { //  && entityliving.getCanSpawnHere()
            world.spawnEntityInWorld(entityliving);
            if (!ForgeEventFactory.doSpecialSpawn(entityliving, world, x, y, z)) {
                entityliving.onSpawnWithEgg(null);
            }
        }
    }

}
