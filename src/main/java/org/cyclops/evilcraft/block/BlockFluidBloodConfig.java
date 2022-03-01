package org.cyclops.evilcraft.block;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link BlockFluidBlood}.
 * @author rubensworks
 *
 */
public class BlockFluidBloodConfig extends BlockConfig {

    public BlockFluidBloodConfig() {
        super(
                EvilCraft._instance,
            "blood",
                eConfig -> new BlockFluidBlood(Block.Properties.of(Material.WATER)
                        .noCollission()
                        .strength(100.0F)
                        .noDrops()
                        .randomTicks()),
                (eConfig, block) -> new BlockItem(block, new Item.Properties())
        );
    }
    
}
