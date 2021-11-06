package org.cyclops.evilcraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;

/**
 * Enchantment that stop your tool from being usable when it only has durability left.
 * @author rubensworks
 *
 */
public class EnchantmentUnusing extends Enchantment {

    public EnchantmentUnusing() {
        super(Rarity.VERY_RARE, EnchantmentType.BREAKABLE, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void unusingEvent(LivingAttackEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote() && event.getSource().getTrueSource() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getSource().getTrueSource();
            ItemStack itemStack = entity.getHeldItemMainhand();
            if (EnchantmentHelpers.doesEnchantApply(itemStack, this) > -1) {
                if (EnchantmentUnusing.unuseTool(itemStack)) {
                    event.setCanceled(true);
                    //player.stopUsingItem();
                    return;
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void unusingEvent(BlockEvent.BreakEvent event) {
        Hand hand = event.getPlayer().getActiveHand();
        if (hand != null) {
            if (EnchantmentHelpers.doesEnchantApply(event.getPlayer().getHeldItem(hand), this) > -1) {
                if (event.getPlayer() != null
                        && EnchantmentUnusing.unuseTool(event.getPlayer().getHeldItem(hand))) {
                    event.setCanceled(true);
                    event.getPlayer().stopActiveHand();
                }
            }
        }
    }
    
    @Override
    public int getMinEnchantability(int par1) {
        return 10;
    }
    
    @Override
    public int getMaxEnchantability(int par1) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApply(ItemStack itemStack) {
        return !itemStack.isEmpty() && (!itemStack.getItem().getToolTypes(itemStack).isEmpty() || itemStack.getItem() instanceof SwordItem);
    }
    
    /**
     * Check if the given item can be used.
     * @param itemStack The {@link ItemStack} that will be unused.
     * @return If the item can be used.
     */
    public static boolean unuseTool(ItemStack itemStack) {
        int damageBorder = itemStack.getMaxDamage() - 5;
        if(itemStack.getDamage() >= damageBorder) {
            itemStack.setDamage(damageBorder);
            return true;
        }
        return false;
    }

}
