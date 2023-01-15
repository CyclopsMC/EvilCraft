package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for the {@link ItemVeinSword}.
 * @author rubensworks
 *
 */
public class ItemVeinSwordConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The multiply boost this sword has on the blood that is obtained.", isCommandable = true)
    public static double extractionBoost = 2.0;

    @ConfigurableProperty(category = "item", comment = "Maximum uses for this item.")
    public static int durability = 32;

    public ItemVeinSwordConfig() {
        super(
                EvilCraft._instance,
                "vein_sword",
                eConfig -> new ItemVeinSword(new Item.Properties()
                        )
        );
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        return Collections.singleton(((ItemVeinSword) getInstance()).getEnchantedItemStack());
    }
}
