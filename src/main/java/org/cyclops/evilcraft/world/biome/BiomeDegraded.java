package org.cyclops.evilcraft.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBiome;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.Helpers;

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
        super(constructProperties(eConfig.downCast())
                .setBaseHeight(0.125F).setTemperature(0.8F)
                .setRainfall(0.9F).setWaterColor(Helpers.RGBToInt(60, 50, 20)),
                eConfig.downCast());
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos blockPos) {
        double d0 = (double) MathHelper.clamp_float(this.getFloatTemperature(blockPos), 0.0F, 1.0F);
        double d1 = (double) MathHelper.clamp_float(this.getRainfall(), 0.0F, 1.0F);
        return ((ColorizerGrass.getGrassColor(d0, d1) & Helpers.RGBToInt(10, 20, 5)) + 5115470) / 2;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos blockPos) {
        double d0 = (double) MathHelper.clamp_float(this.getFloatTemperature(blockPos), 0.0F, 1.0F);
        double d1 = (double) MathHelper.clamp_float(this.getRainfall(), 0.0F, 1.0F);
        return ((ColorizerFoliage.getFoliageColor(d0, d1) & Helpers.RGBToInt(10, 20, 50)) + 5115470) / 2;
    }

    @Override
    public float getSpawningChance()
    {
        return 0.5F;
    }

}
