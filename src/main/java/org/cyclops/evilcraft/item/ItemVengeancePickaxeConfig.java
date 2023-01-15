package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

    @ConfigurableProperty(category = "item", comment = "The default fortune enchantment level on these pickaxes.", requiresMcRestart = true)
    public static int fortuneLevel = 5;

    @ConfigurableProperty(category = "item", comment = "The default vengeance enchantment level on these pickaxes.", requiresMcRestart = true)
    public static int vengeanceLevel = 3;

    public ItemVengeancePickaxeConfig() {
        super(
                EvilCraft._instance,
            "vengeance_pickaxe",
                eConfig -> new ItemVengeancePickaxe(new Item.Properties()

                        .durability(154))
        );
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        return Collections.singleton(((ItemVengeancePickaxe) getInstance()).getEnchantedItemStack());
    }

}
