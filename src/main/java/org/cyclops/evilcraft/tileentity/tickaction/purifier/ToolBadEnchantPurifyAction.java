package org.cyclops.evilcraft.tileentity.tickaction.purifier;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableEnchantment;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierAction;
import org.cyclops.evilcraft.enchantment.EnchantmentVengeance;
import org.cyclops.evilcraft.item.VengeancePickaxe;
import org.cyclops.evilcraft.tileentity.TilePurifier;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.DamageableItemRepairAction;

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
    public boolean canWork(TilePurifier tile) {
        if(!tile.getPurifyItem().isEmpty() && tile.getBucketsFloored() > 0) {
            // Check curses
            for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(tile.getPurifyItem()).entrySet()) {
                if (entry.getKey().isCurse()) {
                    return true;
                }
            }

            // Check bad enchant registry
            for(ConfigurableEnchantment enchant : DamageableItemRepairAction.BAD_ENCHANTS) {
                if (tile.getPurifyItem().getItem() != VengeancePickaxe.getInstance()
                        || enchant != EnchantmentVengeance.getInstance()) {
                    int enchantmentListID = EnchantmentHelpers.doesEnchantApply(tile.getPurifyItem(), enchant);
                    if (enchantmentListID >= 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean removeEnchant(World world, TilePurifier tile, ItemStack purifyItem, int tick, Enchantment enchant) {
        int enchantmentListID = EnchantmentHelpers.doesEnchantApply(purifyItem, enchant);
        if(enchantmentListID > -1) {
            if(tick >= PURIFY_DURATION) {
                if(!world.isRemote) {
                    int level = EnchantmentHelpers.getEnchantmentLevel(purifyItem, enchantmentListID);
                    EnchantmentHelpers.setEnchantmentLevel(purifyItem, enchantmentListID, level - 1);
                }
                tile.setBuckets(tile.getBucketsFloored() - 1, tile.getBucketsRest());
                return true;
            }
            if(world.isRemote) {
                tile.showEffect();
            }
        }
        return false;
    }

    @Override
    public boolean work(TilePurifier tile) {
        boolean done = false;

        ItemStack purifyItem = tile.getPurifyItem();
        World world = tile.getWorld();
        int tick = tile.getTick();

        // Try removing curses
        for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(tile.getPurifyItem()).entrySet()) {
            if (!done && entry.getKey().isCurse()) {
                done = removeEnchant(world, tile, purifyItem, tick, entry.getKey());
            }
        }

        // Try removing bad enchants.
        for(ConfigurableEnchantment enchant : DamageableItemRepairAction.BAD_ENCHANTS) {
            if(!done) {
                if (tile.getPurifyItem().getItem() != VengeancePickaxe.getInstance()
                        || enchant != EnchantmentVengeance.getInstance()) {
                    done = removeEnchant(world, tile, purifyItem, tick, enchant);
                }
            }
        }

        return done;
    }

}
