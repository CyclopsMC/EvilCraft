package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.ItemBloodExtractorConfig;

/**
 * Config for the {@link BlockSpikedPlate}.
 * @author rubensworks
 *
 */
public class BlockSpikedPlateConfig extends BlockConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "block", comment = "The multiplier for amount of mB to receive per mob HP.", isCommandable = true)
    public static double mobMultiplier = ItemBloodExtractorConfig.maximumMobMultiplier;

    @ConfigurablePropertyCommon(category = "block", comment = "The amount of damage per time.", isCommandable = true)
    public static double damage = 4.0D;

    public BlockSpikedPlateConfig() {
        super(
                EvilCraft._instance,
            "spiked_plate",
                (eConfig, properties) -> new BlockSpikedPlate(properties
                        .requiresCorrectToolForDrops()
                        .strength(2.0F)
                        .sound(SoundType.STONE)
                        .noCollision()
                        .isValidSpawn((state, level, pos, type) -> false)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
