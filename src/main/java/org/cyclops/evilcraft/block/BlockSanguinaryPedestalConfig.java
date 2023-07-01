package org.cyclops.evilcraft.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

/**
 * Config for the {@link BlockSanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class BlockSanguinaryPedestalConfig extends BlockConfig {

    @ConfigurableProperty(category = "item", comment = "Blood multiplier when Efficiency is active.", isCommandable = true)
    public static double efficiencyBoost = 1.5D;

    public BlockSanguinaryPedestalConfig(int tier) {
        super(
                EvilCraft._instance,
            "sanguinary_pedestal_" + tier,
                eConfig -> new BlockSanguinaryPedestal(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
                        .sound(SoundType.STONE)
                        .noOcclusion(), tier),
                (eConfig, block) -> new ItemBlockFluidContainer(block, (new Item.Properties())
                        )
        );
    }

}
