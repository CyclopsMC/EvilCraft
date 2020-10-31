package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Potentia Sphere.
 * @author rubensworks
 *
 */
public class ItemPotentiaSphereConfig extends ItemConfig {

    @ConfigurableProperty(category = "general", comment = "If crafting of the ender pearl should be enabled.", requiresMcRestart = true)
    public static boolean enderPearlRecipe = true;

    public ItemPotentiaSphereConfig() {
        super(
                EvilCraft._instance,
            "potentia_sphere",
                eConfig -> new Item(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
    }
    
}
