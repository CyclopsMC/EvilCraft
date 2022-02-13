package org.cyclops.evilcraft.world.gen.structure;

import com.google.common.collect.ImmutableSet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.WorldStructureConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Locale;

/**
 * Config for the {@link WorldStructureDarkTemple}.
 * @author rubensworks
 *
 */
public class WorldStructureDarkTempleConfig extends WorldStructureConfig {

    @ConfigurableProperty(category = "worldgeneration", comment = "If dark temple should be added to all dimensions (except for the end and nether).", configLocation = ModConfig.Type.SERVER)
    public static boolean enabled = true;
    @ConfigurableProperty(category = "worldgeneration", comment = "Minimum block height at which a dark temple can spawn.", configLocation = ModConfig.Type.SERVER)
    public static int darkTempleMinHeight = 64;
    @ConfigurableProperty(category = "worldgeneration", comment = "Maximum blockState height at which a dark temple can spawn.", configLocation = ModConfig.Type.SERVER)
    public static int darkTempleMaxHeight = 90;
    @ConfigurableProperty(category = "worldgeneration", comment = "Average distance between dark temples in chunks.", configLocation = ModConfig.Type.SERVER)
    public static int darkTempleSpacing = 40;
    @ConfigurableProperty(category = "worldgeneration", comment = "Minimum distance between dark temples in chunks, must be smaller than spacing.", configLocation = ModConfig.Type.SERVER)
    public static int darkTempleSeparation = 16;

    public static final IStructurePieceType PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "evilcraft:dark_temple_piece", WorldStructureDarkTemple.Piece::new);
    public static StructureFeature<?, ?> CONFIGURED_FEATURE;

    public WorldStructureDarkTempleConfig() {
        super(
                EvilCraft._instance,
                "dark_temple",
                eConfig -> new WorldStructureDarkTemple(NoFeatureConfig.CODEC)
        );
        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoadingEvent);
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        CONFIGURED_FEATURE = Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE,
                new ResourceLocation(getMod().getModId(), getNamedId() + "_default"),
                ((WorldStructureDarkTemple) getInstance())
                        .configured(IFeatureConfig.NONE));

        Structure.STRUCTURES_REGISTRY.put(getRegistryKey().location().toString().toLowerCase(Locale.ROOT), getInstance());

        // MCP params: spacing, separation, salt
        StructureSeparationSettings settings = new StructureSeparationSettings(darkTempleSpacing, darkTempleSeparation, 370458167) {
            @Override
            public int spacing() {
                return darkTempleSpacing;
            }

            @Override
            public int separation() {
                return darkTempleSeparation;
            }
        };

        ImmutableSet.of(DimensionSettings.OVERWORLD, DimensionSettings.AMPLIFIED, DimensionSettings.NETHER,
                DimensionSettings.END, DimensionSettings.CAVES, DimensionSettings.FLOATING_ISLANDS)
                .stream()
                .map(WorldGenRegistries.NOISE_GENERATOR_SETTINGS::get)
                .map(DimensionSettings::structureSettings)
                .map(DimensionStructuresSettings::structureConfig) // get map
                .forEach(m -> m.put(getInstance(), settings));
    }

    public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        if (WorldStructureDarkTempleConfig.enabled && event.getCategory() != Biome.Category.THEEND && event.getCategory() != Biome.Category.NETHER) {
            event.getGeneration().getStructures().add(() -> CONFIGURED_FEATURE);
        }
    }
}
