package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemCreativeBloodDrop}.
 * @author rubensworks
 *
 */
public class ItemCreativeBloodDropConfig extends ItemConfig {

    public ItemCreativeBloodDropConfig() {
        super(
                EvilCraft._instance,
            "creative_blood_drop",
                eConfig -> new ItemCreativeBloodDrop(new Item.Properties()
                        .tab(EvilCraft._instance.getDefaultItemGroup()))
        );
    }
    
}
