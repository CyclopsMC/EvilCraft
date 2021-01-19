package org.cyclops.evilcraft.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.List;

/**
 * Config for the {@link BlockPurifier}.
 * @author rubensworks
 *
 */
public class BlockPurifierConfig extends BlockConfig {

    @ConfigurableProperty(category = "machine", comment = "Item that can not be disenchanted. Regular expressions are allowed.", isCommandable = true)
    public static List<String> disenchantBlacklist = Lists.newArrayList(
            "tetra:.*"
    );

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
