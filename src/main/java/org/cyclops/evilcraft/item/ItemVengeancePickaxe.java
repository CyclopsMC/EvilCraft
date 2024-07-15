package org.cyclops.evilcraft.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A strong pickaxe that may call up spirits.
 * @author rubensworks
 *
 */
public class ItemVengeancePickaxe extends PickaxeItem {

    public ItemVengeancePickaxe(Properties properties) {
        super(Tiers.DIAMOND, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return super.getDestroySpeed(stack, state) * 1.250F;
    }

    public ItemStack getEnchantedItemStack(HolderLookup.Provider holders) {
        ItemStack pickaxe = new ItemStack(this);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(holders.holderOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("minecraft:fortune"))), ItemVengeancePickaxeConfig.fortuneLevel);
        enchantments.set(holders.holderOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("evilcraft:vengeance"))), ItemVengeancePickaxeConfig.vengeanceLevel);
        EnchantmentHelper.setEnchantments(pickaxe, enchantments.toImmutable());
        return pickaxe;
    }

}
