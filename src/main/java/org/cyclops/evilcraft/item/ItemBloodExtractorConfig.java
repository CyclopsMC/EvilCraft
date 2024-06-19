package org.cyclops.evilcraft.item;


import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

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
                        )
        );
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        // Register items dynamically into tab, because when this is called, capabilities are not initialized yet.
        return Collections.emptyList();
    }

    protected void fillCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == EvilCraft._instance.getDefaultCreativeTab() || event.getTabKey().equals(CreativeModeTabs.SEARCH)) {
            for (ItemStack itemStack : dynamicCreativeTabEntries()) {
                event.accept(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }

    protected Collection<ItemStack> dynamicCreativeTabEntries() {
        return ((ItemBloodExtractor) getInstance()).getDefaultCreativeTabEntries();
    }

}
