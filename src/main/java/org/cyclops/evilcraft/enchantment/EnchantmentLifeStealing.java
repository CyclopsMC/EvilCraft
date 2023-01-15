package org.cyclops.evilcraft.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Enchantment that steals the HP when hit another Entity.
 * @author rubensworks
 *
 */
public class EnchantmentLifeStealing extends Enchantment {

    public EnchantmentLifeStealing() {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void stealLife(LivingAttackEvent event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() && event.getSource().getEntity() instanceof Player) {
            Player player = (Player) event.getSource().getEntity();
            ItemStack itemStack = player.getMainHandItem();
            int enchantmentListID = EnchantmentHelpers.doesEnchantApply(itemStack, this);
            if (enchantmentListID > -1) {
                float damage = event.getAmount();
                int level = EnchantmentHelpers.getEnchantmentLevel(itemStack, enchantmentListID);
                EnchantmentLifeStealing.stealLife(player, damage, level);
                return;
            }
        }
    }

    @Override
    public int getMinCost(int level) {
        return 15 + (level - 1) * 15;
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
     * Transfer the damage dealt as HP to the attacking entity.
     * @param entity The entity that attacked.
     * @param damage The damage that was dealt.
     * @param level The level of the enchant.
     */
    public static void stealLife(LivingEntity entity, float damage, int level) {
        entity.heal(damage / RegistryEntries.ENCHANTMENT_LIFE_STEALING.getMaxLevel()
                * (level + 1) * (float) EnchantmentLifeStealingConfig.healModifier);
    }

}
