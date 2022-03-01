package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemMaceOfDestruction}.
 * @author rubensworks
 *
 */
public class ItemMaceOfDestructionConfig extends ItemConfig {

    public ItemMaceOfDestructionConfig() {
        super(
                EvilCraft._instance,
            "mace_of_destruction",
                eConfig -> new ItemMaceOfDestruction(new Item.Properties()
                        .tab(EvilCraft._instance.getDefaultItemGroup()))
        );
    }
    
}
