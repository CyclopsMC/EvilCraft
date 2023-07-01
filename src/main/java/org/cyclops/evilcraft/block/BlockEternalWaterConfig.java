package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockBloodChest}.
 * @author rubensworks
 *
 */
public class BlockEternalWaterConfig extends BlockConfig {

    public BlockEternalWaterConfig() {
        super(
                EvilCraft._instance,
            "eternal_water",
                eConfig -> new BlockEternalWater(Block.Properties.of()
                        .strength(0.5F)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
