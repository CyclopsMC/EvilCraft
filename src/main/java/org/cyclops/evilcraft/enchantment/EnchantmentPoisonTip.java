package org.cyclops.evilcraft.enchantment;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;

/**
 * Enchantment that poisons the attacked entity.
 * @author rubensworks
 *
 */
public class EnchantmentPoisonTip extends Enchantment {

    private static final int POISON_BASE_DURATION = 2;

    public EnchantmentPoisonTip() {
        super(Rarity.RARE, EnchantmentCategory.BOW, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
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
        entity.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_BASE_DURATION * 20 * (level + 1), 1));
    }

}
