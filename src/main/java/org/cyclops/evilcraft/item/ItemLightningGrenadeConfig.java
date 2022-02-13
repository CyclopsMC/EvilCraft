package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemLightningGrenade}.
 * @author rubensworks
 *
 */
public class ItemLightningGrenadeConfig extends ItemConfig {

    public ItemLightningGrenadeConfig() {
        super(
                EvilCraft._instance,
                "lightning_grenade",
                eConfig -> new ItemLightningGrenade(new Item.Properties()
                        .tab(EvilCraft._instance.getDefaultItemGroup())
                        .stacksTo(16))
        );
    }
    
}
