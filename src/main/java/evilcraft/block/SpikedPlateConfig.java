package evilcraft.block;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.item.BloodExtractorConfig;

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
        	true,
            "spikedPlate",
            null,
            SpikedPlate.class
        );
    }
    
}
