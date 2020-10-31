package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import org.cyclops.evilcraft.block.BlockExcrementPile;
import org.cyclops.evilcraft.block.BlockExcrementPileConfig;

/**
 * {@link BlockItem} for the {@link BlockExcrementPile}.
 * @author rubensworks
 *
 */
public class ItemExcrementPile extends BlockItem {

    public ItemExcrementPile(Block block, Item.Properties properties) {
        super(block, properties);
    }
    
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack itemStack = context.getPlayer().getHeldItem(context.getHand());
		Block block = context.getWorld().getBlockState(context.getPos()).getBlock();
        if(context.getPlayer().isCrouching()) {
            boolean done = false;
            int attempts = 0;
            while(attempts < BlockExcrementPileConfig.effectiveness) {
                done = BoneMealItem.applyBonemeal(itemStack.copy(), context.getWorld(), context.getPos(), context.getPlayer()) | done;
                attempts++;
            }
            if(done) {
                itemStack.shrink(1);
                if (!context.getWorld().isRemote()) {
                    context.getWorld().playBroadcastSound(2005, context.getPos(), 0);
                }
                return ActionResultType.SUCCESS;
            }
        } else {
            if (block instanceof BlockExcrementPile && !itemStack.isEmpty()) {
                if(BlockExcrementPile.canHeightenPileAt(context.getWorld(), context.getPos())) {
                    BlockExcrementPile.heightenPileAt(context.getWorld(), context.getPos());
                    itemStack.shrink(1);
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
        }
        return super.onItemUse(context);
    }

}
