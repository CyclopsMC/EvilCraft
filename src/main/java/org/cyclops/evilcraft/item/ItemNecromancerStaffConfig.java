package org.cyclops.evilcraft.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for the {@link ItemNecromancerStaff}.
 * @author rubensworks
 *
 */
public class ItemNecromancerStaffConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The capacity of the container.", requiresMcRestart = true)
    public static int capacity = FluidHelpers.BUCKET_VOLUME * 10;

    @ConfigurableProperty(category = "item", comment = "The amount of Blood that will be drained per usage.", isCommandable = true)
    public static int usage = FluidHelpers.BUCKET_VOLUME * 2;

    public ItemNecromancerStaffConfig() {
        super(
                EvilCraft._instance,
            "necromancer_staff",
                eConfig -> new ItemNecromancerStaff(new Item.Properties()
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
        return ((ItemNecromancerStaff) getInstance()).getDefaultCreativeTabEntries();
    }

}
