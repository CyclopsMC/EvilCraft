package org.cyclops.evilcraft.blockentity.tickaction.bloodchest;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairAction;
import org.cyclops.evilcraft.block.BlockBloodChestConfig;

import java.util.List;

/**
 * Repair action for damageable items.
 * @author rubensworks
 *
 */
public class DamageableItemRepairAction implements IBloodChestRepairAction {

    private static final int CHANCE_RANDOM_ENCHANT = 10000;

    @Override
    public boolean isItemValidForSlot(ItemStack itemStack) {
        return itemStack.isRepairable();
    }

    @Override
    public boolean canRepair(ItemStack itemStack, int tick) {
        return itemStack.isDamaged() && itemStack.isRepairable();
    }

    @Override
    public Pair<Float, ItemStack> repair(ItemStack itemStack, RandomSource random, boolean doAction, boolean isBulk, HolderLookup.Provider holderLookupProvider) {
        if(doAction) {
            // Repair the item
            int newDamage = itemStack.getDamageValue() - 1;
            itemStack.setDamageValue(newDamage);

            // Add bad enchant with a certain chance
            if (!isBulk && BlockBloodChestConfig.addRandomBadEnchants && random.nextInt(CHANCE_RANDOM_ENCHANT) == 0) {
                List<Holder.Reference<Enchantment>> curses = holderLookupProvider.lookupOrThrow(Registries.ENCHANTMENT)
                        .listElements()
                        .filter(p_344414_ -> p_344414_.is(EnchantmentTags.CURSE))
                        .toList();

                Holder<Enchantment> enchantment = curses.get(random.nextInt(curses.size()));
                itemStack.enchant(
                        enchantment,
                        enchantment.value().getMinLevel() + random.nextInt(
                                enchantment.value().getMaxLevel() - enchantment.value().getMinLevel())
                );
            }
        }
        return Pair.of(1F, itemStack);
    }

}
