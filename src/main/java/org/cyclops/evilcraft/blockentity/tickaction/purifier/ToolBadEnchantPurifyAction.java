package org.cyclops.evilcraft.blockentity.tickaction.purifier;

import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierAction;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;
import org.cyclops.evilcraft.core.algorithm.Wrapper;
import org.cyclops.evilcraft.item.ItemVengeancePickaxe;

/**
 * Purifier action to remove enchantments from tools.
 * @author Ruben Taelman
 */
public class ToolBadEnchantPurifyAction implements IPurifierAction {

    private static final int PURIFY_DURATION = 60;

    @Override
    public boolean isItemValidForMainSlot(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isItemValidForAdditionalSlot(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean canWork(BlockEntityPurifier tile) {
        Wrapper<Boolean> done = new Wrapper<>(false);
        if(!tile.getPurifyItem().isEmpty() && tile.getBucketsFloored() > 0) {
            // Check curses
            ItemStack purifyItem = tile.getPurifyItem();
            EnchantmentHelpers.runIterationOnItem(purifyItem, (enchantment, level) -> {
                if (enchantment.is(EnchantmentTags.CURSE) && !(purifyItem.getItem() instanceof ItemVengeancePickaxe)) {
                    done.set(true);
                }
            });
        }
        return done.get();
    }

    protected boolean removeEnchant(Level world, BlockEntityPurifier tile, ItemStack purifyItem, int tick, Holder<Enchantment> enchant, int level) {
        if(tick >= PURIFY_DURATION) {
            if(!world.isClientSide()) {
                EnchantmentHelpers.setEnchantmentLevel(purifyItem, enchant, level - 1);
            }
            tile.setBuckets(tile.getBucketsFloored() - 1, tile.getBucketsRest());
            return true;
        }
        if(world.isClientSide()) {
            tile.showEffect();
        }
        return false;
    }

    @Override
    public boolean work(BlockEntityPurifier tile) {
        Wrapper<Boolean> done = new Wrapper<>(false);

        ItemStack purifyItem = tile.getPurifyItem();
        Level world = tile.getLevel();
        int tick = tile.getTick();

        // Try removing curses
        EnchantmentHelpers.runIterationOnItem(purifyItem, (enchantment, level) -> {
            if (enchantment.is(EnchantmentTags.CURSE) && !(purifyItem.getItem() instanceof ItemVengeancePickaxe)) {
                if (removeEnchant(world, tile, purifyItem, tick, enchantment, level)) {
                    done.set(true);
                }
            }
        });

        return done.get();
    }

}
