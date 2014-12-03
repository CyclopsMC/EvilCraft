package evilcraft.core.block;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

/**
 * Implement this interface at blocks to make them show rarity colors in item mode.
 * @author rubensworks
 */
public interface IBlockRarityProvider {

    /**
     * @param itemStack The itemstack with this block.
     * @return The rarity for this block.
     */
    public EnumRarity getRarity(ItemStack itemStack);

}
