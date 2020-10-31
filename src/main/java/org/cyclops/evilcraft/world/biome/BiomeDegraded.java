package org.cyclops.evilcraft.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.Helpers;

/**
 * Enchantment for letting tools break tools faster.
 * @author rubensworks
 *
 */
public class BiomeDegraded extends Biome {

    public BiomeDegraded() {
        super(new Biome.Builder()
                .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                .precipitation(RainType.NONE)
                .category(Category.THEEND)
                .depth(0.125F)
                .scale(0.4F)
                .temperature(0.8F)
                .downfall(0.9F)
                .waterColor(Helpers.RGBToInt(60, 50, 20))
                .waterFogColor(Helpers.RGBToInt(60, 50, 20))
                .parent(null));
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public int getGrassColor(double p_225528_1_, double p_225528_3_) {
        return Helpers.RGBToInt(10, 20, 5);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getFoliageColor() {
        return Helpers.RGBToInt(10, 20, 50);
    }

    @Override
    public float getSpawningChance()
    {
        return 0.5F;
    }

}
