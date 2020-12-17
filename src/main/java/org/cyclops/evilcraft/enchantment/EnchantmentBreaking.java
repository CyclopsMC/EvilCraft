package org.cyclops.evilcraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.DamageableItemRepairAction;

import java.util.Random;

/**
 * Enchantment for letting tools break tools faster.
 * @author rubensworks
 *
 */
public class EnchantmentBreaking extends Enchantment {

    public EnchantmentBreaking() {
        super(Rarity.COMMON, EnchantmentType.BREAKABLE, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
        DamageableItemRepairAction.BAD_ENCHANTS.add(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void breakingEvent(LivingAttackEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote() && event.getSource().getTrueSource() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getSource().getTrueSource();
            ItemStack itemStack = entity.getHeldItem(entity.getActiveHand());
            int enchantmentListID = EnchantmentHelpers.doesEnchantApply(itemStack, this);
            if (enchantmentListID > -1) {
                EnchantmentBreaking.amplifyDamage(itemStack, enchantmentListID, new Random());
                entity.setHeldItem(entity.getActiveHand(), itemStack);
                return;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void breakingEvent(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().world.isRemote()) {
            int i = EnchantmentHelpers.doesEnchantApply(event.getPlayer().getHeldItem(event.getPlayer().getActiveHand()), this);
            ItemStack itemStack = event.getPlayer().getHeldItem(event.getPlayer().getActiveHand());
            EnchantmentBreaking.amplifyDamage(itemStack, i, new Random());
            event.getPlayer().setHeldItem(event.getPlayer().getActiveHand(), itemStack);
        }
    }
    
    @Override
    public int getMinEnchantability(int level) {
        return 1 + (level - 1) * 8;
    }
    
    @Override
    public int getMaxEnchantability(int level) {
        return super.getMinEnchantability(level) + 50;
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
            int newDamage = itemStack.getDamage() + 2;
            if(random.nextFloat() < 0.6F ? false : random.nextInt(level + 1) > 0
                    && newDamage <= itemStack.getMaxDamage()) {
                itemStack.setDamage(newDamage);
            }
        }
    }

}
