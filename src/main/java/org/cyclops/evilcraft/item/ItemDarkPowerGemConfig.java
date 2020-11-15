package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Dark Power Gem.
 * @author rubensworks
 *
 */
public class ItemDarkPowerGemConfig extends ItemConfig {

    public ItemDarkPowerGemConfig() {
        super(
                EvilCraft._instance,
            "dark_power_gem",
                eConfig -> new Item(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .maxStackSize(16))
        );
    }
    
}
