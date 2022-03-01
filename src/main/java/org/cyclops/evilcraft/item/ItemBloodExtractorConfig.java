package org.cyclops.evilcraft.item;


import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemBloodExtractor}.
 * @author rubensworks
 *
 */
public class ItemBloodExtractorConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The minimum multiplier for amount of mB to receive per mob HP.", isCommandable = true)
    public static double minimumMobMultiplier = 5;
    @ConfigurableProperty(category = "item", comment = "The maximum multiplier for amount of mB to receive per mob HP. IMPORTANT: must be larger than minimumMobMultiplier!", isCommandable = true)
    public static double maximumMobMultiplier = 40;
    @ConfigurableProperty(category = "item", comment = "The amount of blood (mB) this container can hold.", requiresMcRestart = true)
    public static int containerSize = 5000;
    @ConfigurableProperty(category = "item", comment = "If held buckets should be autofilled when enabled.", isCommandable = true)
    public static boolean autoFillBuckets = false;

    public ItemBloodExtractorConfig() {
        super(
                EvilCraft._instance,
                "blood_extractor",
                eConfig -> new ItemBloodExtractor(new Item.Properties()
                        .tab(EvilCraft._instance.getDefaultItemGroup()))
        );
    }

}
