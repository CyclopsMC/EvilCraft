package org.cyclops.evilcraft.block;


import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for {@link BlockFluidPoison}.
 * @author rubensworks
 *
 */
public class BlockFluidPoisonConfig extends BlockConfig {

    public BlockFluidPoisonConfig() {
        super(
                EvilCraft._instance,
            "poison",
                eConfig -> new BlockFluidPoison(Block.Properties.of()
                        .liquid()
                        .noCollission()
                        .strength(100.0F)
                        .randomTicks()),
                (eConfig, block) -> new BlockItem(block, new Item.Properties())
        );
    }

    @Override
    protected Collection<ItemStack> defaultCreativeTabEntries() {
        return Collections.emptyList();
    }

}
