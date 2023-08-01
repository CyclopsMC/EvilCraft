package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.core.algorithm.OrganicSpread;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.entity.item.EntityBiomeExtract;

/**
 * Makes biomes darker.
 * @author rubensworks
 *
 */
public class BiomeDegradation implements IDegradationEffect, OrganicSpread.IOrganicSpreadable {

    private static final int DIMENSIONS = 2;

    public BiomeDegradation(DegradationEffectConfig eConfig) {

    }

    @Override
    public boolean canRun(IDegradable degradable) {
        return true;
    }

    @Override
    public void runClientSide(IDegradable degradable) {

    }

    @Override
    public void runServerSide(IDegradable degradable) {
        OrganicSpread spread =
                new OrganicSpread(degradable.getDegradationWorld(), DIMENSIONS, degradable.getRadius(), this);
        spread.spreadTick(LocationHelpers.copyLocation(degradable.getLocation()));
    }

    @Override
    public boolean isDone(Level world, BlockPos location) {
        return world.getBiome(location).value() == BiomeDegradation.getBiome(world);
    }

    @Override
    public void spreadTo(Level world, BlockPos location) {
        if (!world.isClientSide()) {
            EntityBiomeExtract.setBiome((ServerLevel) world, location, BiomeDegradation.getBiome(world));
            EntityBiomeExtract.updateChunkAfterBiomeChange(world, new ChunkPos(location));
        }
    }

    public static Biome getBiome(Level level) {
        return level.registryAccess().registryOrThrow(Registries.BIOME).get(RegistryEntries.BIOME_DEGRADED_KEY);
    }

}
