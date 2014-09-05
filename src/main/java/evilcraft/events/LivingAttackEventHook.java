package evilcraft.events;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.Configs;
import evilcraft.core.helpers.EnchantmentHelpers;
import evilcraft.enchantment.EnchantmentBreaking;
import evilcraft.enchantment.EnchantmentBreakingConfig;
import evilcraft.enchantment.EnchantmentLifeStealing;
import evilcraft.enchantment.EnchantmentLifeStealingConfig;
import evilcraft.enchantment.EnchantmentPoisonTip;
import evilcraft.enchantment.EnchantmentPoisonTipConfig;
import evilcraft.enchantment.EnchantmentUnusing;
import evilcraft.enchantment.EnchantmentUnusingConfig;

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
        if(Configs.isEnabled(EnchantmentLifeStealingConfig.class) && event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            ItemStack itemStack = player.getCurrentEquippedItem();
            int enchantmentListID = EnchantmentHelpers.doesEnchantApply(itemStack, EnchantmentLifeStealingConfig._instance.ID);
            if(enchantmentListID > -1) {
                float damage = event.ammount;
                int level = EnchantmentHelpers.getEnchantmentLevel(itemStack, enchantmentListID);
                EnchantmentLifeStealing.stealLife(player, damage, level);
            }
        }
    }
    
    private void unusingEvent(LivingAttackEvent event) {
        if(Configs.isEnabled(EnchantmentUnusingConfig.class) && event.source.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.source.getEntity();
            ItemStack itemStack = entity.getEquipmentInSlot(0);
            if(EnchantmentHelpers.doesEnchantApply(itemStack, EnchantmentUnusingConfig._instance.ID) > -1) {
                if(entity != null
                        && EnchantmentUnusing.unuseTool(itemStack)) {
                    event.setCanceled(true);
                    //player.stopUsingItem();
                }
            }
        }
    }
    
    private void breakingEvent(LivingAttackEvent event) {
        if(Configs.isEnabled(EnchantmentBreakingConfig.class) && event.source.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.source.getEntity();
            ItemStack itemStack = entity.getEquipmentInSlot(0);
            int enchantmentListID = EnchantmentHelpers.doesEnchantApply(itemStack, EnchantmentBreakingConfig._instance.ID);
            if(enchantmentListID > -1) {
                EnchantmentBreaking.amplifyDamage(itemStack, enchantmentListID, new Random());
            }
        }
    }
    
    private void poisonTipEvent(LivingAttackEvent event) {
        if(Configs.isEnabled(EnchantmentPoisonTipConfig.class) && event.source.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.source.getEntity();
            ItemStack itemStack = entity.getEquipmentInSlot(0);
            int enchantmentListID = EnchantmentHelpers.doesEnchantApply(itemStack, EnchantmentPoisonTipConfig._instance.ID);
            if(enchantmentListID > -1) {
                int level = EnchantmentHelpers.getEnchantmentLevel(itemStack, enchantmentListID);
                EnchantmentPoisonTip.poison((EntityLivingBase)event.entity, level);
            }
        }
    }
    
}
