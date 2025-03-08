package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemRedstoneGrenade}.
 * @author immortaleeb
 *
 */
public class ItemRedstoneGrenadeConfig extends ItemConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "item", comment = "If the redstone grenade should drop again as an item after it is being thrown.", isCommandable = true)
    public static boolean dropAfterUsage = false;

    public ItemRedstoneGrenadeConfig() {
        super(
                EvilCraft._instance,
                "redstone_grenade",
                (eConfig, properties) -> new ItemRedstoneGrenade(properties)
            );
    }
}
