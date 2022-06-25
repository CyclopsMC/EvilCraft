package org.cyclops.evilcraft.item;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.SwordItem;
import net.minecraft.core.NonNullList;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;

import net.minecraft.world.item.Item.Properties;

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
        super(Tiers.GOLD, 3, -2.4F, properties);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ItemVeinSwordConfig.durability;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (this.allowedIn(group)) {
            items.add(getEnchantedItemStack());
        }
    }

    public ItemStack getEnchantedItemStack() {
        ItemStack sword = new ItemStack(this);
        EnchantmentHelpers.setEnchantmentLevel(sword, Enchantments.MOB_LOOTING, LOOTING_LEVEL);
        return sword;
    }

}
