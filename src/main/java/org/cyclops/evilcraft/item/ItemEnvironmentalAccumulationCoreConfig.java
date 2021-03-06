package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Environmental Accumulation Core.
 * @author rubensworks
 *
 */
public class ItemEnvironmentalAccumulationCoreConfig extends ItemConfig {

    public ItemEnvironmentalAccumulationCoreConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulation_core",
                eConfig -> new Item(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .rarity(Rarity.RARE)) {
                    @Override
                    public boolean hasEffect(ItemStack stack) {
                        return true;
                    }
                }
        );
    }
    
}
