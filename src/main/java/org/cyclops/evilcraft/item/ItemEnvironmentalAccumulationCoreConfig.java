package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
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

                        .rarity(Rarity.RARE)) {
                    @Override
                    public boolean isFoil(ItemStack stack) {
                        return true;
                    }
                }
        );
    }

}
