package evilcraft.event;

import evilcraft.Configs;
import evilcraft.core.helper.EnchantmentHelpers;
import evilcraft.enchantment.EnchantmentBreaking;
import evilcraft.enchantment.EnchantmentBreakingConfig;
import evilcraft.enchantment.EnchantmentUnusing;
import evilcraft.enchantment.EnchantmentUnusingConfig;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

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
        if(Configs.isEnabled(EnchantmentUnusingConfig.class) && doesEnchantApply(event, EnchantmentUnusingConfig._instance.ID) > -1) {
            if(event.entityPlayer != null
                    && EnchantmentUnusing.unuseTool(event.entityPlayer.getCurrentEquippedItem())) {
                event.setCanceled(true);
                event.entityPlayer.stopUsingItem();
            }
        }
    }
    
    private void breakingEvent(PlayerInteractEvent event) {
        if(Configs.isEnabled(EnchantmentBreakingConfig.class)) {
            int i = doesEnchantApply(event, EnchantmentBreakingConfig._instance.ID);
            ItemStack itemStack = event.entityPlayer.getCurrentEquippedItem();
            EnchantmentBreaking.amplifyDamage(itemStack, i, new Random());
        }
    }
    
    private int doesEnchantApply(PlayerInteractEvent event, int enchantID) {
        if(event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {
            if(event.entityPlayer != null) {
                ItemStack itemStack = event.entityPlayer.getCurrentEquippedItem();
                return EnchantmentHelpers.doesEnchantApply(itemStack, enchantID);
            }
        }
        return -1;
    }
}
