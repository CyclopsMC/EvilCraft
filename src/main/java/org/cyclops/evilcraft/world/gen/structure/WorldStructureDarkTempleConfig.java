package org.cyclops.evilcraft.world.gen.structure;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
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

    public static StructurePieceType PIECE_TYPE;

    public static ResourceKey<Structure> KEY = ResourceKey.create(
            Registries.STRUCTURE,
            new ResourceLocation(Reference.MOD_ID, "dark_temple")
        );

    public WorldStructureDarkTempleConfig() {
        super(
                EvilCraft._instance,
                "dark_temple",
                eConfig -> WorldStructureDarkTemple.CODEC
        );
    }

    @Override
    public void onRegistered() {
        super.onRegistered();

        RegistryEntries.STRUCTURE_DARK_TEMPLE = getInstance();

        PIECE_TYPE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, "evilcraft:dark_temple_piece", (StructurePieceType) (StructurePieceType.ContextlessType) WorldStructureDarkTemple.Piece::new);
    }
}
