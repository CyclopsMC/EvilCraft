package evilcraft.biomes;

import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.config.BiomeConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBiome;

/**
 * Enchantment for letting tools break tools faster.
 * @author rubensworks
 *
 */
public class BiomeDegraded extends ConfigurableBiome {
    
    private static BiomeDegraded _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BiomeConfig> eConfig) {
        if(_instance == null)
            _instance = new BiomeDegraded(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BiomeDegraded getInstance() {
        return _instance;
    }

    private BiomeDegraded(ExtendedConfig<BiomeConfig> eConfig) {
        super(eConfig.downCast());
        this.setHeight(height_MidPlains);
        this.setTemperatureRainfall(0.8F, 0.9F);
        this.setColor(Helpers.RGBToInt(0, 30, 20));
        this.func_76733_a(Helpers.RGBToInt(20, 50, 30));
        this.waterColorMultiplier = Helpers.RGBToInt(60, 50, 20);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getBiomeGrassColor(int x, int y, int z) {
        double d0 = (double)this.getFloatTemperature(x, y, z);
        double d1 = (double)this.getFloatRainfall();
        return ((ColorizerGrass.getGrassColor(d0, d1) & Helpers.RGBToInt(10, 20, 5)) + 5115470) / 2;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBiomeFoliageColor(int x, int y, int z) {
        double d0 = (double)this.getFloatTemperature(x, y, z);
        double d1 = (double)this.getFloatRainfall();
        return ((ColorizerFoliage.getFoliageColor(d0, d1) & Helpers.RGBToInt(10, 20, 50)) + 5115470) / 2;
    }

}
