package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockLightningBomb}.
 * @author rubensworks
 *
 */
public class BlockLightningBombConfig extends BlockConfig {

    public BlockLightningBombConfig() {
        super(
                EvilCraft._instance,
            "lightning_bomb",
                eConfig -> new BlockLightningBomb(Block.Properties.of()
                        .strength(0.0F)
                        .sound(SoundType.GRAVEL)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
