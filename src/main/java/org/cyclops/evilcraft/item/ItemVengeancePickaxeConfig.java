package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .maxDamage(154))
        );
    }
    
}
