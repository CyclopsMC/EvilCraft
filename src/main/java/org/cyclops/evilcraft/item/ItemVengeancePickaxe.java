package org.cyclops.evilcraft.item;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.NonNullList;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Map;

/**
 * A strong pickaxe that may call up spirits.
 * @author rubensworks
 *
 */
public class ItemVengeancePickaxe extends PickaxeItem {

    public ItemVengeancePickaxe(Properties properties) {
        super(ItemTier.DIAMOND, 1, -2.8F, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return super.getDestroySpeed(stack, state) * 1.250F;
    }

    // Can break all blocks, like diamond
    @Override
    public boolean canHarvestBlock(BlockState blockState) {
        return true;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            ItemStack pickaxe = new ItemStack(this);
            Map<Enchantment, Integer> enchantments = Maps.newHashMap();
            enchantments.put(Enchantments.FORTUNE, ItemVengeancePickaxeConfig.fortuneLevel);
            enchantments.put(RegistryEntries.ENCHANTMENT_VENGEANCE, ItemVengeancePickaxeConfig.vengeanceLevel);
            EnchantmentHelper.setEnchantments(enchantments, pickaxe);
        }
    }

}
