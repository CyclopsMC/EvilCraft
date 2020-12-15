package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

/**
 * Config for the {@link BlockPurifier}.
 * @author rubensworks
 *
 */
public class BlockPurifierConfig extends BlockConfig {

    public BlockPurifierConfig() {
        super(
                EvilCraft._instance,
            "purifier",
                eConfig -> new BlockPurifier(Block.Properties.create(Material.IRON)
                        .hardnessAndResistance(2.5F)
                        .sound(SoundType.STONE)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, (new Item.Properties())
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
    }
    
}
