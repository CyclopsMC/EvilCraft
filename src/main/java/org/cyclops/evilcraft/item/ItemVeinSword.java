package org.cyclops.evilcraft.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

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
        super(Tiers.GOLD, properties);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ItemVeinSwordConfig.durability;
    }

    public ItemStack getEnchantedItemStack(HolderLookup.Provider holders) {
        ItemStack sword = new ItemStack(this);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(holders.holderOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("minecraft:looting"))), LOOTING_LEVEL);
        EnchantmentHelper.setEnchantments(sword, enchantments.toImmutable());
        return sword;
    }

}
