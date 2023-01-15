package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;

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
                        )
        );
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        return ((ItemBloodPearlOfTeleportation) getInstance()).getDefaultCreativeTabEntries();
    }

}
