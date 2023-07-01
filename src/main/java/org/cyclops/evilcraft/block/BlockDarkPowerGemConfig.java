package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the dark power gem block.
 * @author rubensworks
 *
 */
public class BlockDarkPowerGemConfig extends BlockConfig {

    public BlockDarkPowerGemConfig() {
        super(
                EvilCraft._instance,
            "dark_power_gem_block",
                eConfig -> new Block(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(5.0F)
                        .sound(SoundType.METAL)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
