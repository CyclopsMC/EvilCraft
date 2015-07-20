package org.cyclops.evilcraft.block;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.BloodExtractorConfig;

/**
 * Config for the {@link SpikedPlate}.
 * @author rubensworks
 *
 */
public class SpikedPlateConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static SpikedPlateConfig _instance;
    
    /**
     * The multiplier for amount of mB to receive per mob HP.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK, comment = "The multiplier for amount of mB to receive per mob HP.", isCommandable = true)
    public static double mobMultiplier = BloodExtractorConfig.maximumMobMultiplier;
    
    /**
     * The amount of damage per time.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK, comment = "The amount of damage per time.", isCommandable = true)
    public static double damage = 4.0D;

    /**
     * Make a new instance.
     */
    public SpikedPlateConfig() {
        super(
                EvilCraft._instance,
        	true,
            "spikedPlate",
            null,
            SpikedPlate.class
        );
    }
    
}
