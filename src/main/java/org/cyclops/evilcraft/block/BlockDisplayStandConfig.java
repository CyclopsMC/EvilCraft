package org.cyclops.evilcraft.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;

/**
 * Config for the {@link BlockDisplayStand}.
 * @author rubensworks
 *
 */
public class BlockDisplayStandConfig extends BlockConfig {

    public BlockDisplayStandConfig() {
        super(
                EvilCraft._instance,
            "display_stand",
                eConfig -> new BlockDisplayStand(Block.Properties.of()
                                .requiresCorrectToolForDrops()),
                (eConfig, block) -> new BlockItem(block, (new Item.Properties())
                        )
        );
    }

    @Override
    protected Collection<ItemStack> defaultCreativeTabEntries() {
        NonNullList<ItemStack> list = NonNullList.create();
        ((BlockDisplayStand) getInstance()).fillItemCategory(list);
        return list;
    }

}
