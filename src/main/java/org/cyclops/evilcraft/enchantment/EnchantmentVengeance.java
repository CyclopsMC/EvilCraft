package org.cyclops.evilcraft.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.config.configurable.ConfigurableEnchantment;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.item.VengeanceRing;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.DamageableItemRepairAction;

/**
 * Enchantment for letting tools break tools faster.
 * @author rubensworks
 *
 */
public class EnchantmentVengeance extends ConfigurableEnchantment {

    private static EnchantmentVengeance _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EnchantmentVengeance getInstance() {
        return _instance;
    }

    public EnchantmentVengeance(ExtendedConfig<EnchantmentConfig> eConfig) {
        super(eConfig, Rarity.COMMON, EnumEnchantmentType.ALL, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        DamageableItemRepairAction.BAD_ENCHANTS.add(this);
        MinecraftForge.EVENT_BUS.register(this);
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
        EntityPlayer player = event.getPlayer();
        if (player != null && !player.world.isRemote) {
            ItemStack heldItem = player.getHeldItem(player.getActiveHand());
            int level = getEnchantLevel(heldItem);
            if (level > 0) {
                apply(player.world, level, player);
            }
        }
    }

    @SubscribeEvent
    public void onAttack(LivingAttackEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity instanceof EntityPlayer && !entity.world.isRemote) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack heldItem = player.getHeldItem(player.getActiveHand());
            int level = getEnchantLevel(heldItem);
            if (level > 0) {
                apply(player.world, level, player);
            }
        }
    }

    public static int getEnchantLevel(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentVengeance.getInstance(), itemStack);
    }

    public static void apply(World world, int level, EntityLivingBase entity) {
        if (level > 0) {
            int chance = Math.max(1, EnchantmentVengeanceConfig.vengeanceChance / level);
            if (chance > 0 && world.rand.nextInt(chance) == 0) {
                int area = EnchantmentVengeanceConfig.areaOfEffect * level;
                VengeanceRing.toggleVengeanceArea(world, entity, area, true, true, true);
            }
        }
    }

}
