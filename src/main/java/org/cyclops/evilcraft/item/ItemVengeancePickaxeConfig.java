package org.cyclops.evilcraft.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for the {@link ItemVengeancePickaxe}.
 * @author rubensworks
 *
 */
public class ItemVengeancePickaxeConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The default fortune enchantment level on these pickaxes in the creative tab.", requiresMcRestart = true)
    public static int fortuneLevel = 5;

    @ConfigurableProperty(category = "item", comment = "The default vengeance enchantment level on these pickaxes in the creative tab.", requiresMcRestart = true)
    public static int vengeanceLevel = 3;

    public ItemVengeancePickaxeConfig() {
        super(
                EvilCraft._instance,
            "vengeance_pickaxe",
                eConfig -> new ItemVengeancePickaxe(new Item.Properties()
                        .attributes(PickaxeItem.createAttributes(Tiers.DIAMOND, 1, -2.8F))
                        .durability(154))
        );
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        return Collections.emptyList();
    }

    protected Collection<ItemStack> dynamicCreativeTabEntries(CreativeModeTab.ItemDisplayParameters parameters) {
        return Collections.singleton(((ItemVengeancePickaxe) getInstance()).getEnchantedItemStack(parameters.holders()));
    }

    protected void fillCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == EvilCraft._instance.getDefaultCreativeTab()) {
            for (ItemStack itemStack : dynamicCreativeTabEntries(event.getParameters())) {
                event.accept(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }

}
