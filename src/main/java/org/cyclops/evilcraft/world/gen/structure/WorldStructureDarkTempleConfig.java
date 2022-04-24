package org.cyclops.evilcraft.world.gen.structure;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.WorldStructureConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

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
    // TODO: re-implement configuration of spacing and separation somehow (with custom placement type instead of RandomSpreadStructurePlacement?)
    //@ConfigurableProperty(category = "worldgeneration", comment = "Average distance between dark temples in chunks.", configLocation = ModConfig.Type.SERVER)
    public static int darkTempleSpacing = 40;
    //@ConfigurableProperty(category = "worldgeneration", comment = "Minimum distance between dark temples in chunks, must be smaller than spacing.", configLocation = ModConfig.Type.SERVER)
    public static int darkTempleSeparation = 16;

    public static final ResourceKey<StructureSet> STRUCTURE_SET_ID = ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, new ResourceLocation(Reference.MOD_ID, "dark_temple"));
    public static Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_FEATURE;
    public static Holder<StructurePieceType> PIECE_TYPE;
    public static Holder<StructureSet> STRUCTURE_SET;

    public WorldStructureDarkTempleConfig() {
        super(
                EvilCraft._instance,
                "dark_temple",
                eConfig -> new WorldStructureDarkTemple(NoneFeatureConfiguration.CODEC)
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        PIECE_TYPE = BuiltinRegistries.register(Registry.STRUCTURE_PIECE, "evilcraft:dark_temple_piece", (StructurePieceType) (StructurePieceType.ContextlessType) WorldStructureDarkTemple.Piece::new);

        CONFIGURED_FEATURE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
                new ResourceLocation(getMod().getModId(), getNamedId() + "_default"),
                ((WorldStructureDarkTemple) getInstance())
                        .configured(FeatureConfiguration.NONE, TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Reference.MOD_ID, "has_structure/dark_temple"))));

        STRUCTURE_SET = BuiltinRegistries.register(
                BuiltinRegistries.STRUCTURE_SETS,
                STRUCTURE_SET_ID,
                new StructureSet(
                        CONFIGURED_FEATURE, new RandomSpreadStructurePlacement(darkTempleSpacing, darkTempleSeparation, RandomSpreadType.TRIANGULAR, 370458167)
                ));
    }
}
