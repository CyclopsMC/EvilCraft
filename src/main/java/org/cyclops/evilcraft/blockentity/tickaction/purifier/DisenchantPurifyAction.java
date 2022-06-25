package org.cyclops.evilcraft.blockentity.tickaction.purifier;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierAction;
import org.cyclops.evilcraft.block.BlockPurifierConfig;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;
import org.cyclops.evilcraft.core.algorithm.Wrapper;

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
            if(ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString().matches(name)) {
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
    public boolean canWork(BlockEntityPurifier tile) {
        if(tile.getBucketsFloored() == tile.getMaxBuckets() && !tile.getPurifyItem().isEmpty() &&
                !tile.getAdditionalItem().isEmpty() && tile.getAdditionalItem().getItem() == ALLOWED_BOOK.get()) {
            return isAllowed(tile.getPurifyItem()) && !EnchantmentHelper.getEnchantments(tile.getPurifyItem()).isEmpty();
        }
        return false;
    }

    @Override
    public boolean work(BlockEntityPurifier tile) {
        boolean done = false;

        ItemStack purifyItem = tile.getPurifyItem().copy();
        Level world = tile.getLevel();
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

    private Enchantment getRandomEnchantment(Level world, Map<Enchantment, Integer> enchantments) {
        int enchantmentIndex = world.random.nextInt(enchantments.size());
        return Lists.newArrayList(enchantments.keySet()).get(enchantmentIndex);
    }

    private void setResultingEnchantmentBook(BlockEntityPurifier tile, Map<Enchantment, Integer> enchantments, Enchantment enchantment) {
        tile.setAdditionalItem(EnchantedBookItem.createForEnchantment(
                new EnchantmentInstance(enchantment, enchantments.get(enchantment))));
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
