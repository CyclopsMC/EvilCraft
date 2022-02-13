package org.cyclops.evilcraft.tileentity.tickaction.purifier;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierAction;
import org.cyclops.evilcraft.block.BlockBloodChestConfig;
import org.cyclops.evilcraft.block.BlockPurifierConfig;
import org.cyclops.evilcraft.core.algorithm.Wrapper;
import org.cyclops.evilcraft.tileentity.TilePurifier;

import java.util.Map;

/**
 * Purifier action to remove enchantments from tools.
 * @author Ruben Taelman
 */
public class DisenchantPurifyAction implements IPurifierAction {

    /**
     * The allowed book instance.
     */
    public static final Wrapper<Item> ALLOWED_BOOK = new Wrapper<Item>();

    private static final int PURIFY_DURATION = 60;

    protected boolean isAllowed(ItemStack itemStack) {
        if(itemStack.isEmpty()) return false;
        for(String name : BlockPurifierConfig.disenchantBlacklist) {
            if(itemStack.getItem().getRegistryName().toString().matches(name)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isItemValidForMainSlot(ItemStack itemStack) {
        return isAllowed(itemStack);
    }

    @Override
    public boolean isItemValidForAdditionalSlot(ItemStack itemStack) {
        return !itemStack.isEmpty() && itemStack.getItem() == ALLOWED_BOOK.get();
    }

    @Override
    public boolean canWork(TilePurifier tile) {
        if(tile.getBucketsFloored() == tile.getMaxBuckets() && !tile.getPurifyItem().isEmpty() &&
                !tile.getAdditionalItem().isEmpty() && tile.getAdditionalItem().getItem() == ALLOWED_BOOK.get()) {
            return isAllowed(tile.getPurifyItem()) && !EnchantmentHelper.getEnchantments(tile.getPurifyItem()).isEmpty();
        }
        return false;
    }

    @Override
    public boolean work(TilePurifier tile) {
        boolean done = false;

        ItemStack purifyItem = tile.getPurifyItem().copy();
        World world = tile.getLevel();
        int tick = tile.getTick();

        // Try disenchanting.
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(purifyItem);
        if (!enchantments.isEmpty()) {
            if (tick >= PURIFY_DURATION) {
                if (!world.isClientSide()) {
                    Enchantment enchantment = getRandomEnchantment(world, enchantments);
                    setResultingEnchantmentBook(tile, enchantments, enchantment);
                    removePriorWorkPenalty(enchantments, purifyItem);
                    purifyItem = setRemainingEnchantmentsOnPurifiedItem(enchantments, enchantment, purifyItem);
                    tile.setPurifyItem(purifyItem);
                }
                tile.setBuckets(0, tile.getBucketsRest());
                done = true;
            }
            if (world.isClientSide()) {
                tile.showEffect();
                tile.showEnchantingEffect();
            }
        }
        return done;
    }

    private Enchantment getRandomEnchantment(World world, Map<Enchantment, Integer> enchantments) {
        int enchantmentIndex = world.random.nextInt(enchantments.size());
        return Lists.newArrayList(enchantments.keySet()).get(enchantmentIndex);
    }

    private void setResultingEnchantmentBook(TilePurifier tile, Map<Enchantment, Integer> enchantments, Enchantment enchantment) {
        tile.setAdditionalItem(EnchantedBookItem.createForEnchantment(
                new EnchantmentData(enchantment, enchantments.get(enchantment))));
    }

    private void removePriorWorkPenalty(Map<Enchantment, Integer> enchantments, ItemStack purifyItem) {
        int penalty = purifyItem.getBaseRepairCost();
        int remainingPenalty = penalty -  penalty / enchantments.size();
        purifyItem.setRepairCost(remainingPenalty);
    }

    private ItemStack setRemainingEnchantmentsOnPurifiedItem(Map<Enchantment, Integer> enchantments, Enchantment enchantment, ItemStack purifyItem) {
        Map<Enchantment, Integer> remainingEnchantments = Maps.newHashMap(enchantments);
        remainingEnchantments.remove(enchantment);

        // Hardcoded conversion to a regular book when enchantment list of enchanted book is empty.
        if (remainingEnchantments.isEmpty() && purifyItem.getItem() == Items.ENCHANTED_BOOK) {
            purifyItem = new ItemStack(Items.BOOK);
        }

        if (purifyItem.hasTag() && purifyItem.getTag().contains("StoredEnchantments")) {
            purifyItem.getTag().remove("StoredEnchantments");
        }
        EnchantmentHelper.setEnchantments(remainingEnchantments, purifyItem);
        return purifyItem;
    }
}
