package org.cyclops.evilcraft.event;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

/**
 * Event hook for {@link ItemCraftedEvent}.
 * @author rubensworks
 *
 */
public class ItemCraftedEventHook {
    
    /**
     * When an item crafted event is received.
     * @param event The received event.
     */
	@SubscribeEvent
    public void onCraft(ItemCraftedEvent event) {
        craftDeadBush(event);
    }

    private void craftDeadBush(ItemCraftedEvent event) {
        Item item = event.crafting.getItem();
        if(item != null && item == Item.getItemFromBlock(Blocks.DEADBUSH)) {
            for(int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
                ItemStack stack = event.craftMatrix.getStackInSlot(i);
                if(stack != null && stack.getItem() == Items.SHEARS) {
                    stack = stack.copy();
                    stack.damageItem(1, event.player);
                    if(!event.player.inventory.addItemStackToInventory(stack)) {
                        event.player.dropItem(stack, false);
                    }
                    return;
                }
            }
        }
    }
}
