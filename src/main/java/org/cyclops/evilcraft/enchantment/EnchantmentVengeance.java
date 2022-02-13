package org.cyclops.evilcraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemVengeanceRing;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.DamageableItemRepairAction;

import net.minecraft.enchantment.Enchantment.Rarity;

/**
 * Enchantment for letting tools break tools faster.
 * @author rubensworks
 *
 */
public class EnchantmentVengeance extends Enchantment {

    public EnchantmentVengeance() {
        super(Rarity.COMMON, EnchantmentType.BREAKABLE, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
        DamageableItemRepairAction.BAD_ENCHANTS.add(this);
        MinecraftForge.EVENT_BUS.register(this);
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

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player != null && !player.level.isClientSide()) {
            Hand hand = event.getPlayer().getUsedItemHand();
            if (hand != null) {
                ItemStack heldItem = player.getItemInHand(hand);
                int level = getEnchantLevel(heldItem);
                if (level > 0) {
                    apply(player.level, level, player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttack(LivingAttackEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity instanceof PlayerEntity && !entity.level.isClientSide()) {
            PlayerEntity player = (PlayerEntity) entity;
            Hand hand = player.getUsedItemHand();
            if (hand != null) {
                ItemStack heldItem = player.getItemInHand(hand);
                int level = getEnchantLevel(heldItem);
                if (level > 0) {
                    apply(player.level, level, player);
                }
            }
        }
    }

    public static int getEnchantLevel(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        }
        return EnchantmentHelper.getItemEnchantmentLevel(RegistryEntries.ENCHANTMENT_VENGEANCE, itemStack);
    }

    public static void apply(World world, int level, LivingEntity entity) {
        if (level > 0) {
            int chance = Math.max(1, EnchantmentVengeanceConfig.vengeanceChance / level);
            if (chance > 0 && world.random.nextInt(chance) == 0) {
                int area = EnchantmentVengeanceConfig.areaOfEffect * level;
                ItemVengeanceRing.toggleVengeanceArea(world, entity, area, true, true, true);
            }
        }
    }

}
