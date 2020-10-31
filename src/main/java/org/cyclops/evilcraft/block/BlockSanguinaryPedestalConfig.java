package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                eConfig -> new BlockSanguinaryPedestal(Block.Properties.create(Material.IRON), tier),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
}
