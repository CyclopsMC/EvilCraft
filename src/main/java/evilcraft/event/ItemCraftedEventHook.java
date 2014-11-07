package evilcraft.event;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import evilcraft.Achievements;
import evilcraft.Configs;
import evilcraft.block.SpiritFurnace;
import evilcraft.block.SpiritFurnaceConfig;
import evilcraft.item.BloodExtractor;
import evilcraft.item.BloodExtractorConfig;
import evilcraft.item.ExaltedCrafter;
import evilcraft.item.ExaltedCrafterConfig;
import net.minecraft.item.ItemStack;

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
        craftBloodExtractor(event);
        craftSpiritFurnace(event);
        craftExaltedCrafter(event);
        craftDeadBush(event);
    }
    
    private void craftBloodExtractor(ItemCraftedEvent event) {
    	if(Configs.isEnabled(BloodExtractorConfig.class)) {
	    	Item item = event.crafting.getItem();
	    	if(event.player != null && item != null && item == BloodExtractor.getInstance()) {
	    		event.player.addStat(Achievements.SECOND_AGE, 1);
	        }
    	}
    }
    
    private void craftSpiritFurnace(ItemCraftedEvent event) {
    	if(Configs.isEnabled(SpiritFurnaceConfig.class)) {
	    	Item item = event.crafting.getItem();
	    	if(event.player != null && item != null &&
	    			item == Item.getItemFromBlock(SpiritFurnace.getInstance())) {
	    		event.player.addStat(Achievements.SPIRIT_COOKER, 1);
	        }
    	}
    }
    
    private void craftExaltedCrafter(ItemCraftedEvent event) {
    	if(Configs.isEnabled(ExaltedCrafterConfig.class)) {
	    	Item item = event.crafting.getItem();
	    	if(event.player != null && item != null &&
	    			item == ExaltedCrafter.getInstance()) {
	    		event.player.addStat(Achievements.POWER_CRAFTING, 1);
	        }
    	}
    }

    private void craftDeadBush(ItemCraftedEvent event) {
        Item item = event.crafting.getItem();
        if(item != null && item == Item.getItemFromBlock(Blocks.deadbush)) {
            for(int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
                ItemStack stack = event.craftMatrix.getStackInSlot(i);
                if(stack != null && stack.getItem() == Items.shears) {
                    stack = stack.copy();
                    stack.damageItem(1, event.player);
                    if(!event.player.inventory.addItemStackToInventory(stack)) {
                        event.player.dropPlayerItemWithRandomChoice(stack, false);
                    }
                    return;
                }
            }
        }
    }
}
