package org.cyclops.evilcraft.enchantment;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.level.BlockEvent;
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
        super(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void unusingEvent(LivingAttackEvent event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() && event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getSource().getEntity();
            ItemStack itemStack = entity.getMainHandItem();
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
        InteractionHand hand = event.getPlayer().getUsedItemHand();
        if (hand != null) {
            if (EnchantmentHelpers.doesEnchantApply(event.getPlayer().getItemInHand(hand), this) > -1) {
                if (event.getPlayer() != null
                        && EnchantmentUnusing.unuseTool(event.getPlayer().getItemInHand(hand))) {
                    event.setCanceled(true);
                    event.getPlayer().releaseUsingItem();
                }
            }
        }
    }

    @Override
    public int getMinCost(int par1) {
        return 10;
    }

    @Override
    public int getMaxCost(int par1) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.isDamageableItem();
    }

    /**
     * Check if the given item can be used.
     * @param itemStack The {@link ItemStack} that will be unused.
     * @return If the item can be used.
     */
    public static boolean unuseTool(ItemStack itemStack) {
        int damageBorder = itemStack.getMaxDamage() - 5;
        if(itemStack.getDamageValue() >= damageBorder) {
            itemStack.setDamageValue(damageBorder);
            return true;
        }
        return false;
    }

}
