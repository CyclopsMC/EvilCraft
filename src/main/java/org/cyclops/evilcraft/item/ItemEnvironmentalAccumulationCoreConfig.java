package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Environmental Accumulation Core.
 * @author rubensworks
 *
 */
public class ItemEnvironmentalAccumulationCoreConfig extends ItemConfigCommon<IModBase> {

    public ItemEnvironmentalAccumulationCoreConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulation_core",
                (eConfig, properties) -> new Item(properties
                        .rarity(Rarity.RARE)) {
                    @Override
                    public boolean isFoil(ItemStack stack) {
                        return true;
                    }
                }
        );
    }

}
