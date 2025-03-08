package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Garmonbozia.
 * @author rubensworks
 *
 */
public class ItemGarmonboziaConfig extends ItemConfigCommon<IModBase> {

    public ItemGarmonboziaConfig() {
        super(
                EvilCraft._instance,
            "garmonbozia",
                (eConfig, properties) -> new Item(properties
                        .rarity(Rarity.EPIC)) {
                    @Override
                    public boolean isFoil(ItemStack stack) {
                        return true;
                    }
                }
        );
    }
}
