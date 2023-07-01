package org.cyclops.evilcraft.enchantment;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.tickaction.bloodchest.DamageableItemRepairAction;
import org.cyclops.evilcraft.item.ItemVengeanceRing;

/**
 * Enchantment for letting tools break tools faster.
 * @author rubensworks
 *
 */
public class EnchantmentVengeance extends Enchantment {

    public EnchantmentVengeance() {
        super(Rarity.COMMON, EnchantmentCategory.BREAKABLE, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
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
        Player player = event.getPlayer();
        if (player != null && !player.level().isClientSide()) {
            InteractionHand hand = event.getPlayer().getUsedItemHand();
            if (hand != null) {
                ItemStack heldItem = player.getItemInHand(hand);
                int level = getEnchantLevel(heldItem);
                if (level > 0) {
                    apply(player.level(), level, player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttack(LivingAttackEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity instanceof Player && !entity.level().isClientSide()) {
            Player player = (Player) entity;
            InteractionHand hand = player.getUsedItemHand();
            if (hand != null) {
                ItemStack heldItem = player.getItemInHand(hand);
                int level = getEnchantLevel(heldItem);
                if (level > 0) {
                    apply(player.level(), level, player);
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

    public static void apply(Level world, int level, LivingEntity entity) {
        if (level > 0) {
            int chance = Math.max(1, EnchantmentVengeanceConfig.vengeanceChance / level);
            if (chance > 0 && world.random.nextInt(chance) == 0) {
                int area = EnchantmentVengeanceConfig.areaOfEffect * level;
                ItemVengeanceRing.toggleVengeanceArea(world, entity, area, true, true, true);
            }
        }
    }

}
