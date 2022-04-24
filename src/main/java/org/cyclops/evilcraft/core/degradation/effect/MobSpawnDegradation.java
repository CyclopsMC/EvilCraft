package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.core.degradation.StochasticDegradationEffect;

import java.util.Optional;

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
        ServerLevel world = (ServerLevel) degradable.getDegradationWorld();
        BlockPos spawn = LocationHelpers.getRandomPointInSphere(degradable.getLocation(), degradable.getRadius());
        float x = spawn.getX() + 0.5F;
        float y = spawn.getY();
        float z = spawn.getZ() + 0.5F;
        Optional<MobSpawnSettings.SpawnerData> spawnlistentry = world.getBiome(spawn).value().getMobSettings().getMobs(MobCategory.MONSTER).getRandom(world.random);
        Mob entityliving;

        try {
            entityliving = (Mob)spawnlistentry.get().type.create(world);
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        entityliving.moveTo((double)x, (double)y, (double)z, world.random.nextFloat() * 360.0F, 0.0F);
        EntityHelpers.spawnEntity(world, entityliving, MobSpawnType.MOB_SUMMONED);
    }

}
