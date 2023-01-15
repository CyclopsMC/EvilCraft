package org.cyclops.evilcraft.core.block;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

/**
 * Implement this interface at blocks to make them show rarity colors in item mode.
 * @author rubensworks
 */
public interface IBlockRarityProvider {

    /**
     * @param itemStack The itemstack with this blockState.
     * @return The rarity for this blockState.
     */
    public Rarity getRarity(ItemStack itemStack);

}
