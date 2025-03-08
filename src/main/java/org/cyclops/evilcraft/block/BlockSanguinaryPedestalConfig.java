package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

/**
 * Config for the {@link BlockSanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class BlockSanguinaryPedestalConfig extends BlockConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "item", comment = "Blood multiplier when Efficiency is active.", isCommandable = true)
    public static double efficiencyBoost = 1.5D;

    public BlockSanguinaryPedestalConfig(int tier) {
        super(
                EvilCraft._instance,
            "sanguinary_pedestal_" + tier,
                (eConfig, properties) -> new BlockSanguinaryPedestal(properties
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
                        .sound(SoundType.STONE)
                        .noOcclusion(), tier),
                (eConfig, block) -> new ItemBlockFluidContainer(block, eConfig.createDefaultItemProperties())
        );
    }

}
