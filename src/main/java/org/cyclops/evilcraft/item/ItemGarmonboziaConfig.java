package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Garmonbozia.
 * @author rubensworks
 *
 */
public class ItemGarmonboziaConfig extends ItemConfig {

    public ItemGarmonboziaConfig() {
        super(
                EvilCraft._instance,
            "garmonbozia",
                eConfig -> new Item(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .rarity(Rarity.EPIC)) {
                    @Override
                    public boolean hasEffect(ItemStack stack) {
                        return true;
                    }
                }
        );
    }
}
