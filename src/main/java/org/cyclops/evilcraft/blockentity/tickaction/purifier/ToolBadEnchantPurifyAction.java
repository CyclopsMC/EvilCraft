package org.cyclops.evilcraft.blockentity.tickaction.purifier;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierAction;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;
import org.cyclops.evilcraft.blockentity.tickaction.bloodchest.DamageableItemRepairAction;
import org.cyclops.evilcraft.enchantment.EnchantmentVengeance;
import org.cyclops.evilcraft.item.ItemVengeancePickaxe;

import java.util.Map;

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
        if(!tile.getPurifyItem().isEmpty() && tile.getBucketsFloored() > 0) {
            // Check curses
            for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(tile.getPurifyItem()).entrySet()) {
                if (entry.getKey().isCurse()) {
                    return true;
                }
            }

            // Check bad enchant registry
            for(Enchantment enchant : DamageableItemRepairAction.BAD_ENCHANTS) {
                if (!(tile.getPurifyItem().getItem() instanceof ItemVengeancePickaxe)
                        || (enchant instanceof EnchantmentVengeance)) {
                    int enchantmentListID = EnchantmentHelpers.doesEnchantApply(tile.getPurifyItem(), enchant);
                    if (enchantmentListID >= 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean removeEnchant(Level world, BlockEntityPurifier tile, ItemStack purifyItem, int tick, Enchantment enchant) {
        int enchantmentListID = EnchantmentHelpers.doesEnchantApply(purifyItem, enchant);
        if(enchantmentListID > -1) {
            if(tick >= PURIFY_DURATION) {
                if(!world.isClientSide()) {
                    int level = EnchantmentHelpers.getEnchantmentLevel(purifyItem, enchantmentListID);
                    EnchantmentHelpers.setEnchantmentLevel(purifyItem, enchantmentListID, level - 1);
                }
                tile.setBuckets(tile.getBucketsFloored() - 1, tile.getBucketsRest());
                return true;
            }
            if(world.isClientSide()) {
                tile.showEffect();
            }
        }
        return false;
    }

    @Override
    public boolean work(BlockEntityPurifier tile) {
        boolean done = false;

        ItemStack purifyItem = tile.getPurifyItem();
        Level world = tile.getLevel();
        int tick = tile.getTick();

        // Try removing curses
        for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(tile.getPurifyItem()).entrySet()) {
            if (!done && entry.getKey().isCurse()) {
                done = removeEnchant(world, tile, purifyItem, tick, entry.getKey());
            }
        }

        // Try removing bad enchants.
        for(Enchantment enchant : DamageableItemRepairAction.BAD_ENCHANTS) {
            if(!done) {
                if (!(tile.getPurifyItem().getItem() instanceof ItemVengeancePickaxe)
                        || (enchant instanceof EnchantmentVengeance)) {
                    done = removeEnchant(world, tile, purifyItem, tick, enchant);
                }
            }
        }

        return done;
    }

}
