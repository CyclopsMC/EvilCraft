package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockDarkOre}.
 * @author rubensworks
 *
 */
public class BlockDarkOreConfig extends BlockConfig {

    public BlockDarkOreConfig(boolean deepslate) {
        super(
                EvilCraft._instance,
            "dark_ore" + (deepslate ? "_deepslate" : ""),
                eConfig -> new BlockDarkOre(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(3.0F)
                        .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
