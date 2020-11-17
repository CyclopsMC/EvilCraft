package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.ItemBloodExtractorConfig;

/**
 * Config for the {@link BlockSpikedPlate}.
 * @author rubensworks
 *
 */
public class BlockSpikedPlateConfig extends BlockConfig {

    @ConfigurableProperty(category = "block", comment = "The multiplier for amount of mB to receive per mob HP.", isCommandable = true)
    public static double mobMultiplier = ItemBloodExtractorConfig.maximumMobMultiplier;

    @ConfigurableProperty(category = "block", comment = "The amount of damage per time.", isCommandable = true)
    public static double damage = 4.0D;

    public BlockSpikedPlateConfig() {
        super(
                EvilCraft._instance,
            "spiked_plate",
                eConfig -> new BlockSpikedPlate(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(2.0F)
                        .sound(SoundType.STONE)
                        .doesNotBlockMovement()),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
}
