package org.cyclops.evilcraft.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for the {@link ItemMaceOfDestruction}.
 * @author rubensworks
 *
 */
public class ItemMaceOfDestructionConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The attack speed modifier of this mace, which is added to the default player attack speed of 4.", isCommandable = true)
    public static double attackSpeed = -3.0;

    public ItemMaceOfDestructionConfig() {
        super(
                EvilCraft._instance,
            "mace_of_destruction",
                eConfig -> new ItemMaceOfDestruction(new Item.Properties()
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
        if (event.getTab() == EvilCraft._instance.getDefaultCreativeTab()) {
            for (ItemStack itemStack : dynamicCreativeTabEntries()) {
                event.accept(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }

    protected Collection<ItemStack> dynamicCreativeTabEntries() {
        return ((ItemMaceOfDestruction) getInstance()).getDefaultCreativeTabEntries();
    }

}
