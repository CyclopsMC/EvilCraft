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
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack itemStack = context.getItem();
        boolean done = false;
        int attempts = 0;
        while (attempts < 2) {
            done = BoneMealItem.applyBonemeal(itemStack.copy(), context.getWorld(), context.getPos(), context.getPlayer()) | done;
            attempts++;
        }
        if (done) {
            itemStack.shrink(1);
            if (!context.getWorld().isRemote()) {
                context.getWorld().playBroadcastSound(2005, context.getPos(), 0);
            }
            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
    }

}
