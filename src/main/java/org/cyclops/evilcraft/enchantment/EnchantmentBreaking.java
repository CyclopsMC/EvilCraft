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
import org.cyclops.evilcraft.blockentity.tickaction.bloodchest.DamageableItemRepairAction;

import java.util.Random;

/**
 * Enchantment for letting tools break tools faster.
 * @author rubensworks
 *
 */
public class EnchantmentBreaking extends Enchantment {

    public EnchantmentBreaking() {
        super(Rarity.COMMON, EnchantmentCategory.BREAKABLE, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
        DamageableItemRepairAction.BAD_ENCHANTS.add(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void breakingEvent(LivingAttackEvent event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() && event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getSource().getEntity();
            InteractionHand hand = entity.getUsedItemHand();
            if (hand != null) {
                ItemStack itemStack = entity.getItemInHand(hand);
                int enchantmentListID = EnchantmentHelpers.doesEnchantApply(itemStack, this);
                if (enchantmentListID > -1) {
                    EnchantmentBreaking.amplifyDamage(itemStack, enchantmentListID, new Random());
                    entity.setItemInHand(hand, itemStack);
                    return;
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void breakingEvent(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().level().isClientSide()) {
            InteractionHand hand = event.getPlayer().getUsedItemHand();
            if (hand != null) {
                int i = EnchantmentHelpers.doesEnchantApply(event.getPlayer().getItemInHand(hand), this);
                ItemStack itemStack = event.getPlayer().getItemInHand(hand);
                EnchantmentBreaking.amplifyDamage(itemStack, i, new Random());
                event.getPlayer().setItemInHand(hand, itemStack);
            }
        }
    }

    @Override
    public int getMinCost(int level) {
        return 1 + (level - 1) * 8;
    }

    @Override
    public int getMaxCost(int level) {
        return super.getMinCost(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks()
    {
        return true;
    }

    /**
     * Used by ItemStack.attemptDamageItem. Randomly determines if a point of damage should be amplified using the
     * enchantment level. If the ItemStack is Armor then there is a flat 60% chance for damage to be amplified no
     * matter the enchantment level, otherwise there is a 1-(level/1) chance for damage to be amplified.
     * @param itemStack The ItemStack.
     * @param enchantmentListID Enchantments.
     * @param random A random object.
     */
    public static void amplifyDamage(ItemStack itemStack, int enchantmentListID, Random random) {
        if(enchantmentListID > -1) {
            int level = EnchantmentHelpers.getEnchantmentLevel(itemStack, enchantmentListID);
            int newDamage = itemStack.getDamageValue() + 2;
            if(random.nextFloat() < 0.6F ? false : random.nextInt(level + 1) > 0
                    && newDamage <= itemStack.getMaxDamage()) {
                itemStack.setDamageValue(newDamage);
            }
        }
    }

}
