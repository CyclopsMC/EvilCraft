package org.cyclops.evilcraft.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

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
    public InteractionResult useOn(UseOnContext context) {
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
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

}
