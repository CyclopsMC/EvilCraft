package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
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
    public boolean isDone(World world, BlockPos location) {
        return world.getBiome(location) == RegistryEntries.BIOME_DEGRADED;
    }

    @Override
    public void spreadTo(World world, BlockPos location) {
        if (!world.isClientSide()) {
            EntityBiomeExtract.setBiome((ServerWorld) world, location, RegistryEntries.BIOME_DEGRADED);
            EntityBiomeExtract.updateChunkAfterBiomeChange(world, new ChunkPos(location));
        }
    }
    
}
