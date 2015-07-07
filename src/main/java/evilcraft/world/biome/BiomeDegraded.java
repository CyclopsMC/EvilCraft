package evilcraft.world.biome;

import evilcraft.core.helper.RenderHelpers;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBiome;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * Enchantment for letting tools break tools faster.
 * @author rubensworks
 *
 */
public class BiomeDegraded extends ConfigurableBiome {
    
    private static BiomeDegraded _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BiomeDegraded getInstance() {
        return _instance;
    }

    public BiomeDegraded(ExtendedConfig<BiomeConfig> eConfig) {
        super(eConfig.downCast());
        this.setHeight(height_MidPlains);
        this.setTemperatureRainfall(0.8F, 0.9F);
        this.setColor(RenderHelpers.RGBToInt(0, 30, 20));
        this.func_150563_c(RenderHelpers.RGBToInt(20, 50, 30));
        this.waterColorMultiplier = RenderHelpers.RGBToInt(60, 50, 20);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos blockPos) {
        double d0 = (double)this.getFloatTemperature(blockPos);
        double d1 = (double)this.getFloatRainfall();
        return ((ColorizerGrass.getGrassColor(d0, d1) & RenderHelpers.RGBToInt(10, 20, 5)) + 5115470) / 2;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos blockPos) {
        double d0 = (double)this.getFloatTemperature(blockPos);
        double d1 = (double)this.getFloatRainfall();
        return ((ColorizerFoliage.getFoliageColor(d0, d1) & RenderHelpers.RGBToInt(10, 20, 50)) + 5115470) / 2;
    }

    @Override
    public float getSpawningChance()
    {
        return 0.5F;
    }

}
