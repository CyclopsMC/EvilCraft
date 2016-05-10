package org.cyclops.evilcraft.tileentity.tickaction.purifier;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableEnchantment;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierAction;
import org.cyclops.evilcraft.tileentity.TilePurifier;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.DamageableItemRepairAction;

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
        if(tile.getPurifyItem() != null && tile.getBucketsFloored() > 0) {
            for(ConfigurableEnchantment enchant : DamageableItemRepairAction.BAD_ENCHANTS) {
                int enchantmentListID = EnchantmentHelpers.doesEnchantApply(tile.getPurifyItem(), enchant);
                return enchantmentListID >= 0;
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

        // Try removing bad enchants.
        for(ConfigurableEnchantment enchant : DamageableItemRepairAction.BAD_ENCHANTS) {
            if(!done) {
                int enchantmentListID = EnchantmentHelpers.doesEnchantApply(purifyItem, enchant);
                if(enchantmentListID > -1) {
                    if(tick >= PURIFY_DURATION) {
                        if(!world.isRemote) {
                            int level = EnchantmentHelpers.getEnchantmentLevel(purifyItem, enchantmentListID);
                            EnchantmentHelpers.setEnchantmentLevel(purifyItem, enchantmentListID, level - 1);
                        }
                        tile.setBuckets(tile.getBucketsFloored() - 1, tile.getBucketsRest());
                        done = true;
                    }
                    if(world.isRemote) {
                        tile.showEffect();
                    }
                }
            }
        }

        return done;
    }

}
