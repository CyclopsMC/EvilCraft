package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemBloodPearlOfTeleportation}.
 * @author rubensworks
 *
 */
public class ItemBloodPearlOfTeleportationConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The amount of second slowness should be applied after each teleport.", isCommandable = true)
    public static int slownessDuration = 0;

    public ItemBloodPearlOfTeleportationConfig() {
        super(
                EvilCraft._instance,
            "blood_pearl_of_teleportation",
                eConfig -> new ItemBloodPearlOfTeleportation(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
    }
    
}
