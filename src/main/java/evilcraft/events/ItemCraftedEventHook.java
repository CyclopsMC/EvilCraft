package evilcraft.events;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import evilcraft.Recipes;
import evilcraft.items.BloodContainer;
import evilcraft.items.BloodContainerConfig;

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
    public void onPlayerInteract(ItemCraftedEvent event) {
        craftBloodContainer(event);
    }
    
    private void craftBloodContainer(ItemCraftedEvent event) {
    	if(Recipes.isItemEnabled(BloodContainerConfig.class)) {
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
}
