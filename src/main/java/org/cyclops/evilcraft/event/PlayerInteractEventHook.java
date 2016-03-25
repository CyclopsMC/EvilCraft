package org.cyclops.evilcraft.event;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.enchantment.EnchantmentBreaking;
import org.cyclops.evilcraft.enchantment.EnchantmentBreakingConfig;
import org.cyclops.evilcraft.enchantment.EnchantmentUnusing;
import org.cyclops.evilcraft.enchantment.EnchantmentUnusingConfig;

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
        if(Configs.isEnabled(EnchantmentUnusingConfig.class) && doesEnchantApply(event, EnchantmentUnusingConfig._instance.getEnchantment()) > -1) {
            if(event.entityPlayer != null
                    && EnchantmentUnusing.unuseTool(event.entityPlayer.getActiveItemStack())) {
                event.setCanceled(true);
                event.entityPlayer.stopActiveHand();
            }
        }
    }
    
    private void breakingEvent(PlayerInteractEvent event) {
        if(Configs.isEnabled(EnchantmentBreakingConfig.class)) {
            int i = doesEnchantApply(event, EnchantmentBreakingConfig._instance.getEnchantment());
            ItemStack itemStack = event.entityPlayer.getActiveItemStack();
            EnchantmentBreaking.amplifyDamage(itemStack, i, new Random());
        }
    }
    
    private int doesEnchantApply(PlayerInteractEvent event, Enchantment enchantment) {
        if(event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {
            if(event.entityPlayer != null) {
                ItemStack itemStack = event.entityPlayer.getActiveItemStack();
                return EnchantmentHelpers.doesEnchantApply(itemStack, enchantment);
            }
        }
        return -1;
    }
}
