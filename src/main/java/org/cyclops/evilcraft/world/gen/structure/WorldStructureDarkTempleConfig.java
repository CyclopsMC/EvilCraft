package org.cyclops.evilcraft.world.gen.structure;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.WorldStructureConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;


/**
 * Config for the {@link WorldStructureDarkTemple}.
 * @author rubensworks
 *
 */
public class WorldStructureDarkTempleConfig extends WorldStructureConfig<WorldStructureDarkTemple> {

    @ConfigurableProperty(category = "worldgeneration", comment = "If dark temple should be added to all dimensions (except for the end and nether).", configLocation = ModConfig.Type.SERVER)
    public static boolean enabled = true;
    public static Holder<StructurePieceType> PIECE_TYPE;

    public static ResourceKey<Structure> STRUCTURE_SET = ResourceKey.create(
            BuiltinRegistries.STRUCTURES.key(),
            new ResourceLocation(Reference.MOD_ID, "dark_temples")
        );

    public WorldStructureDarkTempleConfig() {
        super(
                EvilCraft._instance,
                "dark_temple",
                eConfig -> WorldStructureDarkTemple.CODEC
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        RegistryEntries.STRUCTURE_DARK_TEMPLE = getInstance();

        PIECE_TYPE = BuiltinRegistries.register(Registry.STRUCTURE_PIECE, "evilcraft:dark_temple_piece", (StructurePieceType) (StructurePieceType.ContextlessType) WorldStructureDarkTemple.Piece::new);
    }
}
