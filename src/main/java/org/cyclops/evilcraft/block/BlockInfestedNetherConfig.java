package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Locale;

/**
 * Config for the {@link BlockInfestedNether}.
 * @author rubensworks
 *
 */
public class BlockInfestedNetherConfig extends BlockConfig {

    public BlockInfestedNetherConfig(BlockInfestedNether.Type type) {
        super(
            EvilCraft._instance,
            "infested_nether_" + type.name().toLowerCase(Locale.ROOT),
                eConfig -> new BlockInfestedNether(Block.Properties.of()
                        .strength(0.0F), type),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
