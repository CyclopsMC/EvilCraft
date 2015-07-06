package evilcraft.core.degradation.effect;

import evilcraft.api.degradation.IDegradable;
import evilcraft.core.algorithm.OrganicSpread;
import evilcraft.core.algorithm.OrganicSpread.IOrganicSpreadable;
import evilcraft.core.config.configurable.ConfigurableDegradationEffect;
import evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import evilcraft.core.helper.LocationHelpers;
import evilcraft.core.helper.WorldHelpers;
import evilcraft.world.biome.BiomeDegraded;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * Makes biomes darker.
 * @author rubensworks
 *
 */
public class BiomeDegradation extends ConfigurableDegradationEffect implements IOrganicSpreadable {

    private static BiomeDegradation _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<DegradationEffectConfig> eConfig) {
        if(_instance == null)
            _instance = new BiomeDegradation(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
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
    
    private BiomeDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
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
                new OrganicSpread(degradable.getWorld(), DIMENSIONS, degradable.getRadius(), this);
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
