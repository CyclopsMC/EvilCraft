package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
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

                        .stacksTo(16))
        );
    }

}
