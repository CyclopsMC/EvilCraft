package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the dark block.
 * @author rubensworks
 *
 */
public class BlockDarkConfig extends BlockConfig {

    public BlockDarkConfig() {
        super(
                EvilCraft._instance,
            "dark_block",
                eConfig -> new Block(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(5.0F)
                        .sound(SoundType.METAL)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
