package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemSpikeyClaws}.
 * @author rubensworks
 *
 */
public class ItemSpikeyClawsConfig extends ItemConfig {

    public ItemSpikeyClawsConfig() {
        super(
                EvilCraft._instance,
                "spikey_claws",
                eConfig -> new ItemSpikeyClaws(new Item.Properties()
                        .tab(EvilCraft._instance.getDefaultItemGroup())
                        .durability(256))
        );
    }
    
}
