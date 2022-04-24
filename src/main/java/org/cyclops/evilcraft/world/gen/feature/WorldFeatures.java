package org.cyclops.evilcraft.world.gen.feature;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.cyclops.evilcraft.Reference;

/**
 * @author rubensworks
 */
public class WorldFeatures {

    public static <FC extends FeatureConfiguration> Holder<ConfiguredFeature<?, ?>> registerConfigured(String key, ConfiguredFeature<FC, ?> feature) {
        return BuiltinRegistries.register((Registry) BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(Reference.MOD_ID, key), feature);
    }

    public static void load() {
        // Just to trigger class loading
    }

}
