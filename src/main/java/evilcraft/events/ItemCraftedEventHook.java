package evilcraft.events;

import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import evilcraft.Achievements;
import evilcraft.Configs;
import evilcraft.api.item.grenades.IGrenadeType;
import evilcraft.blocks.SpiritFurnace;
import evilcraft.blocks.SpiritFurnaceConfig;
import evilcraft.items.BloodContainer;
import evilcraft.items.BloodContainerConfig;
import evilcraft.items.BloodExtractor;
import evilcraft.items.BloodExtractorConfig;
import evilcraft.items.Grenade;
import evilcraft.items.GrenadeConfig;

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
        craftBloodContainer(event);
        craftBloodExtractor(event);
        craftSpiritFurnace(event);
        craftGrenades(event);
    }

	private void craftGrenades(ItemCraftedEvent event) {
		if (Configs.isEnabled(GrenadeConfig.class)) {
			Item item = event.crafting.getItem();
			if (item != null && item == Grenade.getInstance()) {
				for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
					ItemStack input = event.craftMatrix.getStackInSlot(i);
					if (input != null && input.getItem() == Grenade.getInstance()) {
						List<IGrenadeType> craftedGrenadeTypes = Grenade.deserializeGrenadeTypes(event.crafting);
						List<IGrenadeType> oldGrenadeTypes = Grenade.deserializeGrenadeTypes(input);

						// Add all new grenade types to the old grenade types
						for (IGrenadeType type : craftedGrenadeTypes) {
							oldGrenadeTypes.add(type);
						}

						Grenade.serializeGrenadeTypes(oldGrenadeTypes, event.crafting);
						return;
					}
				}
			}
		}
	}
    
    private void craftBloodContainer(ItemCraftedEvent event) {
    	if(Configs.isEnabled(BloodContainerConfig.class)) {
    		IInventory craftMatrix = event.craftMatrix;
	    	Item item = event.crafting.getItem();
	    	if(item != null && item == BloodContainer.getInstance()) {
	            for(int i = 0; i < craftMatrix.getSizeInventory(); i++) {           
	                if(craftMatrix.getStackInSlot(i) != null) {
	                    ItemStack input = craftMatrix.getStackInSlot(i);
	                    if(input.getItem() != null && input.getItem() == BloodContainer.getInstance()) {
	                        FluidStack inputFluid = BloodContainer.getInstance().getFluid(input);
	                        BloodContainer.getInstance().fill(event.crafting, inputFluid, true);
	                    }
	                }  
	            }
	        }
    	}
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
}
