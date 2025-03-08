package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockDarkOre}.
 * @author rubensworks
 *
 */
public class BlockDarkOreConfig extends BlockConfigCommon<IModBase> {

    public BlockDarkOreConfig(boolean deepslate) {
        super(
                EvilCraft._instance,
            "dark_ore" + (deepslate ? "_deepslate" : ""),
                (eConfig, properties) -> new BlockDarkOre(properties
                        .requiresCorrectToolForDrops()
                        .strength(3.0F)
                        .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
