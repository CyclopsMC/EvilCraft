package org.cyclops.evilcraft.blockentity.tickaction.purifier;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierAction;
import org.cyclops.evilcraft.block.BlockPurifierConfig;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;
import org.cyclops.evilcraft.core.algorithm.Wrapper;

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
            if(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString().matches(name)) {
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
            return isAllowed(tile.getPurifyItem()) && tile.getPurifyItem().get(DataComponents.ENCHANTMENTS) != null;
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
        ItemEnchantments enchantments = purifyItem.get(DataComponents.ENCHANTMENTS);
        if (enchantments != null) {
            if (tick >= PURIFY_DURATION) {
                if (!world.isClientSide()) {
                    Holder<Enchantment> enchantment = getRandomEnchantment(world, enchantments);
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

    private Holder<Enchantment> getRandomEnchantment(Level world, ItemEnchantments enchantments) {
        int enchantmentIndex = world.random.nextInt(enchantments.size());
        return Lists.newArrayList(enchantments.keySet()).get(enchantmentIndex);
    }

    private void setResultingEnchantmentBook(BlockEntityPurifier tile, ItemEnchantments enchantments, Holder<Enchantment> enchantment) {
        tile.setAdditionalItem(EnchantedBookItem.createForEnchantment(
                new EnchantmentInstance(enchantment, enchantments.getLevel(enchantment))));
    }

    private void removePriorWorkPenalty(ItemEnchantments enchantments, ItemStack purifyItem) {
        int penalty = purifyItem.getOrDefault(DataComponents.REPAIR_COST, 0);
        int remainingPenalty = penalty -  penalty / enchantments.size();
        purifyItem.set(DataComponents.REPAIR_COST, remainingPenalty);
    }

    private ItemStack setRemainingEnchantmentsOnPurifiedItem(ItemEnchantments enchantments, Holder<Enchantment> enchantment, ItemStack purifyItem) {
        ItemEnchantments.Mutable enchantmentsMutable = new ItemEnchantments.Mutable(enchantments);
        enchantmentsMutable.set(enchantment, 0);

        // Hardcoded conversion to a regular book when enchantment list of enchanted book is empty.
        if (enchantmentsMutable.keySet().isEmpty() && purifyItem.getItem() == Items.ENCHANTED_BOOK) {
            purifyItem = new ItemStack(Items.BOOK);
        }

        EnchantmentHelper.setEnchantments(purifyItem, enchantmentsMutable.toImmutable());
        return purifyItem;
    }
}
