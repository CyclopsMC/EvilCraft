package org.cyclops.evilcraft.core.block;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

/**
 * Implement this interface at blocks to make them show rarity colors in item mode.
 * @author rubensworks
 */
public interface IBlockRarityProvider {

    /**
     * @param itemStack The itemstack with this blockState.
     * @return The rarity for this blockState.
     */
    public EnumRarity getRarity(ItemStack itemStack);

}
