package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

/**
 * Double coal efficiency.
 * @author rubensworks
 *
 */
public class ItemBloodPotash extends Item {

    public ItemBloodPotash(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        ItemStack itemStack = context.getItemInHand();
        boolean done = false;
        int attempts = 0;
        while (attempts < 2) {
            done = BoneMealItem.applyBonemeal(itemStack.copy(), context.getLevel(), context.getClickedPos(), context.getPlayer()) | done;
            attempts++;
        }
        if (done) {
            itemStack.shrink(1);
            if (!context.getLevel().isClientSide()) {
                context.getLevel().globalLevelEvent(2005, context.getClickedPos(), 0);
            }
            return ActionResultType.SUCCESS;
        }
        return super.useOn(context);
    }

}
