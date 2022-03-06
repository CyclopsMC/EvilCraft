package org.cyclops.evilcraft.world.gen.structure;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.WorldStructureConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    public static final StructurePieceType PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "evilcraft:dark_temple_piece", (StructurePieceType) (StructurePieceType.ContextlessType) WorldStructureDarkTemple.Piece::new);
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_FEATURE;

    public WorldStructureDarkTempleConfig() {
        super(
                EvilCraft._instance,
                "dark_temple",
                eConfig -> new WorldStructureDarkTemple(NoneFeatureConfiguration.CODEC)
        );
        MinecraftForge.EVENT_BUS.addListener(this::addDimensionalSpacing);
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        CONFIGURED_FEATURE = Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
                new ResourceLocation(getMod().getModId(), getNamedId() + "_default"),
                ((WorldStructureDarkTemple) getInstance())
                        .configured(FeatureConfiguration.NONE));

        StructureFeature.STRUCTURES_REGISTRY.put(getResourceKey().location().toString().toLowerCase(Locale.ROOT), getInstance());

        // MCP params: spacing, separation, salt
        StructureFeatureConfiguration settings = new StructureFeatureConfiguration(darkTempleSpacing, darkTempleSeparation, 370458167) {
            @Override
            public int spacing() {
                return darkTempleSpacing;
            }

            @Override
            public int separation() {
                return darkTempleSeparation;
            }
        };

        ImmutableSet.of(NoiseGeneratorSettings.OVERWORLD, NoiseGeneratorSettings.AMPLIFIED, NoiseGeneratorSettings.NETHER,
                NoiseGeneratorSettings.END, NoiseGeneratorSettings.CAVES, NoiseGeneratorSettings.FLOATING_ISLANDS)
                .stream()
                .map(BuiltinRegistries.NOISE_GENERATOR_SETTINGS::get)
                .map(NoiseGeneratorSettings::structureSettings)
                .map(StructureSettings::structureConfig) // get map
                .forEach(m -> m.put(getInstance(), settings));
    }

    // Based on https://github.com/VazkiiMods/Quark/blob/ace90bfcc26db4c50a179f026134e2577987c2b1/src/main/java/vazkii/quark/content/world/module/BigDungeonModule.java
    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if(WorldStructureDarkTempleConfig.enabled && event.getWorld() instanceof ServerLevel serverLevel){
            ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
            if(chunkGenerator instanceof FlatLevelSource && serverLevel.dimension().equals(Level.OVERWORLD))
                return;

            StructureSettings worldStructureConfig = chunkGenerator.getSettings();
            HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> STStructureToMultiMap = new HashMap<>();

            for (Map.Entry<ResourceKey<Biome>, Biome> biomeEntry : serverLevel.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).entrySet()) {
                if (biomeEntry.getValue().getBiomeCategory() != Biome.BiomeCategory.THEEND && biomeEntry.getValue().getBiomeCategory() != Biome.BiomeCategory.NETHER)
                    associateBiomeToConfiguredStructure(STStructureToMultiMap, CONFIGURED_FEATURE, biomeEntry.getKey());
            }

            ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
            worldStructureConfig.configuredStructures.entrySet().stream().filter(entry -> !STStructureToMultiMap.containsKey(entry.getKey())).forEach(tempStructureToMultiMap::put);

            STStructureToMultiMap.forEach((key, value) -> tempStructureToMultiMap.put(key, ImmutableMultimap.copyOf(value)));
            worldStructureConfig.configuredStructures = tempStructureToMultiMap.build();

            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(worldStructureConfig.structureConfig());
            tempMap.putIfAbsent(getInstance(), StructureSettings.DEFAULTS.get(getInstance()));
            worldStructureConfig.structureConfig = tempMap;
        }
    }

    private static void associateBiomeToConfiguredStructure(Map<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> STStructureToMultiMap, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
        STStructureToMultiMap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
        HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap = STStructureToMultiMap.get(configuredStructureFeature.feature);
        if(configuredStructureToBiomeMultiMap.containsValue(biomeRegistryKey))
            EvilCraft.clog(String.format("""
                    Detected 2 ConfiguredStructureFeatures that share the same base StructureFeature trying to be added to same biome. One will be prevented from spawning.
                    This issue happens with vanilla too and is why a Snowy Village and Plains Village cannot spawn in the same biome because they both use the Village base structure.
                    The two conflicting ConfiguredStructures are: %s, %s
                    The biome that is attempting to be shared: %s
                    """,
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature),
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureToBiomeMultiMap.entries().stream().filter(e -> e.getValue() == biomeRegistryKey).findFirst().get().getKey()),
                    biomeRegistryKey), org.apache.logging.log4j.Level.ERROR);

        else configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
    }
}
