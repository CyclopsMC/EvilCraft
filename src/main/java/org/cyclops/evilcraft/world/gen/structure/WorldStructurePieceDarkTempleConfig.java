package org.cyclops.evilcraft.world.gen.structure;

import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.cyclops.cyclopscore.config.extendedconfig.WorldStructurePieceConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;


/**
 * Config for the {@link WorldStructureDarkTemple.Piece}.
 * @author rubensworks
 *
 */
public class WorldStructurePieceDarkTempleConfig extends WorldStructurePieceConfigCommon<IModBase> {
    public WorldStructurePieceDarkTempleConfig() {
        super(
                EvilCraft._instance,
                "dark_temple_piece",
                eConfig -> (StructurePieceType.ContextlessType) WorldStructureDarkTemple.Piece::new
        );
    }
}
