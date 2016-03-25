package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.core.algorithm.OrganicSpread;
import org.cyclops.evilcraft.core.algorithm.OrganicSpread.IOrganicSpreadable;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableDegradationEffect;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.world.biome.BiomeDegraded;

/**
 * Makes biomes darker.
 * @author rubensworks
 *
 */
public class BiomeDegradation extends ConfigurableDegradationEffect implements IOrganicSpreadable {

    private static BiomeDegradation _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BiomeDegradation getInstance() {
        return _instance;
    }
    
    private static final Class<? extends BiomeGenBase> BIOME_CLASS = BiomeDegraded.class;
    private static final BiomeGenBase BIOME = BiomeDegraded.getInstance();
    private static final int DIMENSIONS = 2;
    
    public BiomeDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
        super(eConfig);
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
        return world.getBiomeGenForCoords(location).getClass().equals(BIOME_CLASS);
    }

    @Override
    public void spreadTo(World world, BlockPos location) {
        WorldHelpers.setBiome(world, location, BIOME);
    }
    
}
