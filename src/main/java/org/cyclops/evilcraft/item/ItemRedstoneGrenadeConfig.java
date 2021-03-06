package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemRedstoneGrenade}.
 * @author immortaleeb
 *
 */
public class ItemRedstoneGrenadeConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "If the redstone grenade should drop again as an item after it is being thrown.", isCommandable = true)
    public static boolean dropAfterUsage = false;

    public ItemRedstoneGrenadeConfig() {
        super(
                EvilCraft._instance,
                "redstone_grenade",
                eConfig -> new ItemRedstoneGrenade(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup()))
            );
    }
}
