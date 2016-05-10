package org.cyclops.evilcraft.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableEnchantment;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * Enchantment that stop your tool from being usable when it only has durability left.
 * @author rubensworks
 *
 */
public class EnchantmentUnusing extends ConfigurableEnchantment {
    
    private static EnchantmentUnusing _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EnchantmentUnusing getInstance() {
        return _instance;
    }

    public EnchantmentUnusing(ExtendedConfig<EnchantmentConfig> eConfig) {
        super(eConfig, Rarity.VERY_RARE, EnumEnchantmentType.ALL, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
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
        return itemStack != null && itemStack.getItem().isItemTool(itemStack);
    }
    
    /**
     * Check if the given item can be used.
     * @param itemStack The {@link ItemStack} that will be unused.
     * @return If the item can be used.
     */
    public static boolean unuseTool(ItemStack itemStack) {
        int damageBorder = itemStack.getMaxDamage() - 5;
        if(itemStack.getItemDamage() >= damageBorder) {
            itemStack.setItemDamage(damageBorder);
            return true;
        }
        return false;
    }

}
