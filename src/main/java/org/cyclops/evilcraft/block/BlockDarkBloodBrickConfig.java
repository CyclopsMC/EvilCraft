package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockDarkBloodBrick}.
 * @author rubensworks
 *
 */
public class BlockDarkBloodBrickConfig extends BlockConfig {

    public BlockDarkBloodBrickConfig() {
        super(
                EvilCraft._instance,
            "dark_blood_brick",
                eConfig -> new BlockDarkBloodBrick(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(5.0F)
                        .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
