package evilcraft.event;

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
}
