package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.server.ServerWorld;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.core.degradation.StochasticDegradationEffect;

import java.util.Random;

/**
 * Degradation that will eventually spawn mobs in the area.
 * @author rubensworks
 *
 */
public class MobSpawnDegradation extends StochasticDegradationEffect {
    
    private static final double CHANCE = 0.01D;
    
    public MobSpawnDegradation(DegradationEffectConfig eConfig) {
        super(eConfig, CHANCE);
    }
    
    @Override
    public void runClientSide(IDegradable degradable) {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void runServerSide(IDegradable degradable) {
        ServerWorld world = (ServerWorld) degradable.getDegradationWorld();
        BlockPos spawn = LocationHelpers.getRandomPointInSphere(degradable.getLocation(), degradable.getRadius());
        float x = spawn.getX() + 0.5F;
        float y = spawn.getY();
        float z = spawn.getZ() + 0.5F;
        MobSpawnInfo.Spawners spawnlistentry = WeightedRandom.getRandomItem(new Random(), world.getBiome(spawn).getMobSettings().getMobs(EntityClassification.MONSTER));
        MobEntity entityliving;

        try {
            entityliving = (MobEntity)spawnlistentry.type.create(world);
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        entityliving.moveTo((double)x, (double)y, (double)z, world.random.nextFloat() * 360.0F, 0.0F);
        EntityHelpers.spawnEntity(world, entityliving, SpawnReason.MOB_SUMMONED);
    }

}
