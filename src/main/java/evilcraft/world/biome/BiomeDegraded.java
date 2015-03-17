package evilcraft.world.biome;

import evilcraft.core.config.configurable.ConfigurableBiome;
import evilcraft.core.config.extendedconfig.BiomeConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.RenderHelpers;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        this.setColor(RenderHelpers.RGBToInt(0, 30, 20));
        this.func_150563_c(RenderHelpers.RGBToInt(20, 50, 30));
        this.waterColorMultiplier = RenderHelpers.RGBToInt(60, 50, 20);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    // MCP: getBiomeGrassColor
    public int func_180627_b(BlockPos blockPos) {
        double d0 = (double)this.func_180626_a(blockPos); // MCP: getFloatTemperature
        double d1 = (double)this.getFloatRainfall();
        return ((ColorizerGrass.getGrassColor(d0, d1) & RenderHelpers.RGBToInt(10, 20, 5)) + 5115470) / 2;
    }

    @SideOnly(Side.CLIENT)
    @Override
    // MCP: getBiomeFoliageColor
    public int func_180625_c(BlockPos blockPos) {
        double d0 = (double)this.func_180626_a(blockPos); // MCP: getFloatTemperature
        double d1 = (double)this.getFloatRainfall();
        return ((ColorizerFoliage.getFoliageColor(d0, d1) & RenderHelpers.RGBToInt(10, 20, 50)) + 5115470) / 2;
    }

    @Override
    public float getSpawningChance()
    {
        return 0.5F;
    }

}
