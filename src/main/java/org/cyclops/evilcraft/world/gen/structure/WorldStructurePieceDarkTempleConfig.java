package org.cyclops.evilcraft.world.gen.structure;

import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.cyclops.cyclopscore.config.extendedconfig.WorldStructurePieceConfig;
import org.cyclops.evilcraft.EvilCraft;


/**
 * Config for the {@link WorldStructureDarkTemple.Piece}.
 * @author rubensworks
 *
 */
public class WorldStructurePieceDarkTempleConfig extends WorldStructurePieceConfig {
    public WorldStructurePieceDarkTempleConfig() {
        super(
                EvilCraft._instance,
                "dark_temple_piece",
                eConfig -> (StructurePieceType.ContextlessType) WorldStructureDarkTemple.Piece::new
        );
    }
}
