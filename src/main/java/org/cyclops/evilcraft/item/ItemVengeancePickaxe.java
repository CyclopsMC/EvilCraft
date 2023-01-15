package org.cyclops.evilcraft.item;

import com.google.common.collect.Maps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Map;

/**
 * A strong pickaxe that may call up spirits.
 * @author rubensworks
 *
 */
public class ItemVengeancePickaxe extends PickaxeItem {

    public ItemVengeancePickaxe(Properties properties) {
        super(Tiers.DIAMOND, 1, -2.8F, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return super.getDestroySpeed(stack, state) * 1.250F;
    }

    // Can break all blocks, like diamond
    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return true;
    }

    public ItemStack getEnchantedItemStack() {
        ItemStack pickaxe = new ItemStack(this);
        Map<Enchantment, Integer> enchantments = Maps.newHashMap();
        enchantments.put(Enchantments.BLOCK_FORTUNE, ItemVengeancePickaxeConfig.fortuneLevel);
        enchantments.put(RegistryEntries.ENCHANTMENT_VENGEANCE, ItemVengeancePickaxeConfig.vengeanceLevel);
        EnchantmentHelper.setEnchantments(enchantments, pickaxe);
        return pickaxe;
    }

}
