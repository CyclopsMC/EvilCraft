package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Dark Brick.
 * @author rubensworks
 *
 */
public class BlockDarkBrickConfig extends BlockConfig {

    public BlockDarkBrickConfig() {
        super(
                EvilCraft._instance,
            "dark_brick",
                eConfig -> new Block(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(5.0F)
                        .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
