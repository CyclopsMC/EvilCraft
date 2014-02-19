package evilcraft.events;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.api.Helpers;
import evilcraft.enchantment.EnchantmentBreaking;
import evilcraft.enchantment.EnchantmentBreakingConfig;
import evilcraft.enchantment.EnchantmentUnusing;
import evilcraft.enchantment.EnchantmentUnusingConfig;

/**
 * Event hook for {@link PlayerInteractEvent}.O
 * @author rubensworks
 *
 */
public class PlayerInteractEventHook {
    
    /**
     * When a player interactO event is received.
     * @param event The received event.
     */
	@SubscribeEvent(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        unusingEvent(event);
        breakingEvent(event);
    }
    
    private void unusingEvent(PlayerInteractEvent event) {
        if(doesEnchantApply(event, EnchantmentUnusingConfig._instance.ID) > -1) {
            if(event.entityPlayer != null
                    && EnchantmentUnusing.unuseTool(event.entityPlayer.getCurrentEquippedItem())) {
                event.setCanceled(true);
                event.entityPlayer.stopUsingItem();
            }
        }
    }
    
    private void breakingEvent(PlayerInteractEvent event) {
        int i = doesEnchantApply(event, EnchantmentBreakingConfig._instance.ID);
        ItemStack itemStack = event.entityPlayer.getCurrentEquippedItem();
        EnchantmentBreaking.amplifyDamage(itemStack, i, new Random());
    }
    
    private int doesEnchantApply(PlayerInteractEvent event, int enchantID) {
        if(event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {
            if(event.entityPlayer instanceof EntityPlayer) {
                ItemStack itemStack = event.entityPlayer.getCurrentEquippedItem();
                return Helpers.doesEnchantApply(itemStack, enchantID);
            }
        }
        return -1;
    }
}
