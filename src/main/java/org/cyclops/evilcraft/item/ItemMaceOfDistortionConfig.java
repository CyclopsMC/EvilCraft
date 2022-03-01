package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemMaceOfDistortion}.
 * @author rubensworks
 *
 */
public class ItemMaceOfDistortionConfig extends ItemConfig {

    public ItemMaceOfDistortionConfig() {
        super(
                EvilCraft._instance,
            "mace_of_distortion",
                eConfig -> new ItemMaceOfDistortion(new Item.Properties()
                        .tab(EvilCraft._instance.getDefaultItemGroup()))
        );
    }

}
