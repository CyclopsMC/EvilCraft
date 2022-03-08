package org.cyclops.evilcraft.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                eConfig -> new BlockDisplayStand(Block.Properties.of(Material.WOOD)
                                .requiresCorrectToolForDrops()),
                (eConfig, block) -> new BlockItem(block, (new Item.Properties())
                        .tab(EvilCraft._instance.getDefaultItemGroup()))
        );
    }

}
