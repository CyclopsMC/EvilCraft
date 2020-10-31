package org.cyclops.evilcraft.item;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraft.util.NonNullList;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;

/**
 * A strong pickaxe that may call up spirits.
 * @author rubensworks
 *
 */
public class ItemVeinSword extends SwordItem {
    
    /**
	 * The looting level of this sword.
	 */
	public static final int LOOTING_LEVEL = 2;

    public ItemVeinSword(Properties properties) {
        super(ItemTier.GOLD, 3, -2.4F, properties);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ItemVeinSwordConfig.durability;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            ItemStack sword = new ItemStack(this);
            EnchantmentHelpers.setEnchantmentLevel(sword, Enchantments.LOOTING, LOOTING_LEVEL);
            items.add(sword);
        }
    }

}
