package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Rejuvenated Flesh.
 * @author rubensworks
 *
 */
public class ItemRejuvenatedFleshConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The amount of blood (mB) this container can hold.", requiresMcRestart = true)
    public static int containerSize = 10000;
    @ConfigurableProperty(category = "item", comment = "The amount of blood (mB) that is consumed per bite.")
    public static int biteUsage = 250;

    public ItemRejuvenatedFleshConfig() {
        super(
                EvilCraft._instance,
            "flesh_rejuvenated",
                eConfig -> new ItemRejuvenatedFlesh(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .maxStackSize(1))
        );
    }
    
}
