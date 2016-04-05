package org.cyclops.evilcraft.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.enchantment.*;

import java.util.Random;

/**
 * Event hook for {@link LivingAttackEvent}.
 * @author rubensworks
 *
 */
public class LivingAttackEventHook {

    /**
     * When a living attack event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingAttack(LivingAttackEvent event) {
        stealLife(event);
        unusingEvent(event);
        breakingEvent(event);
        poisonTipEvent(event);
    }
    
    private void stealLife(LivingAttackEvent event) {
        if(Configs.isEnabled(EnchantmentLifeStealingConfig.class) && event.getSource().getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
            ItemStack itemStack = player.getActiveItemStack();
            int enchantmentListID = EnchantmentHelpers.doesEnchantApply(itemStack, EnchantmentLifeStealingConfig._instance.getEnchantment());
            if (enchantmentListID > -1) {
                float damage = event.getAmount();
                int level = EnchantmentHelpers.getEnchantmentLevel(itemStack, enchantmentListID);
                EnchantmentLifeStealing.stealLife(player, damage, level);
                return;
            }
        }
    }
    
    private void unusingEvent(LivingAttackEvent event) {
        if(Configs.isEnabled(EnchantmentUnusingConfig.class) && event.getSource().getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getSource().getEntity();
            ItemStack itemStack = entity.getActiveItemStack();
            if (EnchantmentHelpers.doesEnchantApply(itemStack, EnchantmentUnusingConfig._instance.getEnchantment()) > -1) {
                if (EnchantmentUnusing.unuseTool(itemStack)) {
                    event.setCanceled(true);
                    //player.stopUsingItem();
                    return;
                }
            }
        }
    }
    
    private void breakingEvent(LivingAttackEvent event) {
        if(Configs.isEnabled(EnchantmentBreakingConfig.class) && event.getSource().getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getSource().getEntity();
            ItemStack itemStack = entity.getActiveItemStack();
            int enchantmentListID = EnchantmentHelpers.doesEnchantApply(itemStack, EnchantmentBreakingConfig._instance.getEnchantment());
            if (enchantmentListID > -1) {
                EnchantmentBreaking.amplifyDamage(itemStack, enchantmentListID, new Random());
                return;
            }
        }
    }
    
    private void poisonTipEvent(LivingAttackEvent event) {
        if(Configs.isEnabled(EnchantmentPoisonTipConfig.class) && event.getSource().getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getSource().getEntity();
            ItemStack itemStack = entity.getActiveItemStack();;
            int enchantmentListID = EnchantmentHelpers.doesEnchantApply(itemStack, EnchantmentPoisonTipConfig._instance.getEnchantment());
            if (enchantmentListID > -1) {
                int level = EnchantmentHelpers.getEnchantmentLevel(itemStack, enchantmentListID);
                EnchantmentPoisonTip.poison((EntityLivingBase) event.getEntity(), level);
                return;
            }
        }
    }
    
}
