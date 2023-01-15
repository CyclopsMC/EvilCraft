package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemVengeanceRing}.
 * @author rubensworks
 *
 */
public class ItemVengeanceRingConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The area of effect in # blocks of this ring.", isCommandable = true)
    public static int areaOfEffect = 10;

    public ItemVengeanceRingConfig() {
        super(
                EvilCraft._instance,
            "vengeance_ring",
                eConfig -> new ItemVengeanceRing(new Item.Properties()

                        .stacksTo(1))
        );
    }

}
