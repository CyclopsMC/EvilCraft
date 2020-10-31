package org.cyclops.evilcraft.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                eConfig -> new BlockFluidPoison(Block.Properties.create(Material.WATER)
                        .doesNotBlockMovement()
                        .hardnessAndResistance(100.0F)
                        .noDrops()
                        .tickRandomly()),
                (eConfig, block) -> new BlockItem(block, new Item.Properties())
        );
    }
    
}
