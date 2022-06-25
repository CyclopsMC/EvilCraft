package org.cyclops.evilcraft.block;


import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
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
                eConfig -> new BlockFluidPoison(Block.Properties.of(Material.WATER)
                        .noCollission()
                        .strength(100.0F)
                        .randomTicks()),
                (eConfig, block) -> new BlockItem(block, new Item.Properties())
        );
    }

}
