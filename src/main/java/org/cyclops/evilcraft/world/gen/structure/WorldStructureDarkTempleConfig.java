package org.cyclops.evilcraft.world.gen.structure;

import org.cyclops.cyclopscore.config.extendedconfig.WorldStructureConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;


/**
 * Config for the {@link WorldStructureDarkTemple}.
 * @author rubensworks
 *
 */
public class WorldStructureDarkTempleConfig extends WorldStructureConfigCommon<WorldStructureDarkTemple, IModBase> {
    public WorldStructureDarkTempleConfig() {
        super(
                EvilCraft._instance,
                "dark_temple",
                eConfig -> WorldStructureDarkTemple.CODEC
        );
    }
}
