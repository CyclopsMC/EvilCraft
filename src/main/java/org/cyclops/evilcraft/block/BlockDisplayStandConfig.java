package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderItemStackTileEntityBloodChest;

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
                eConfig -> new BlockDisplayStand(Block.Properties.create(Material.WOOD)),
                (eConfig, block) -> new BlockItem(block, (new Item.Properties())
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .setISTER(() -> RenderItemStackTileEntityBloodChest::new))
        );
    }

}
