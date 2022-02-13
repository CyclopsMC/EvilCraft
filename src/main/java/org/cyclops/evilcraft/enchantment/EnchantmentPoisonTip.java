package org.cyclops.evilcraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;

import net.minecraft.enchantment.Enchantment.Rarity;

/**
 * Enchantment that poisons the attacked entity.
 * @author rubensworks
 *
 */
public class EnchantmentPoisonTip extends Enchantment {
    
    private static final int POISON_BASE_DURATION = 2;

    public EnchantmentPoisonTip() {
        super(Rarity.RARE, EnchantmentType.BOW, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void poisonTipEvent(LivingAttackEvent event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() && event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getSource().getEntity();
            ItemStack itemStack = entity.getMainHandItem();
            int enchantmentListID = EnchantmentHelpers.doesEnchantApply(itemStack, this);
            if (enchantmentListID > -1) {
                int level = EnchantmentHelpers.getEnchantmentLevel(itemStack, enchantmentListID);
                EnchantmentPoisonTip.poison((LivingEntity) event.getEntity(), level);
                return;
            }
        }
    }
    
    @Override
    public int getMinCost(int level) {
        return 10 + (level - 1) * 10;
    }
    
    @Override
    public int getMaxCost(int level) {
        return super.getMinCost(level) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    /**
     * Poison the attacked entity according to the level of the enchant.
     * @param entity The entity was attacked.
     * @param level The level of the enchant.
     */
    public static void poison(LivingEntity entity, int level) {
        entity.addEffect(new EffectInstance(Effects.POISON, POISON_BASE_DURATION * 20 * (level + 1), 1));
    }

}
