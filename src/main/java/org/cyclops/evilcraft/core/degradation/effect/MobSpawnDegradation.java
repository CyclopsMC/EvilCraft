package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.core.degradation.StochasticDegradationEffect;

/**
 * Degradation that will eventually spawn mobs in the area.
 * @author rubensworks
 *
 */
public class MobSpawnDegradation extends StochasticDegradationEffect {

    private static MobSpawnDegradation _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static MobSpawnDegradation getInstance() {
        return _instance;
    }
    
    private static final double CHANCE = 0.01D;
    
    public MobSpawnDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
        super(eConfig, CHANCE);
    }
    
    @Override
    public void runClientSide(IDegradable degradable) {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void runServerSide(IDegradable degradable) {
        WorldServer world = (WorldServer) degradable.getDegradationWorld();
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
