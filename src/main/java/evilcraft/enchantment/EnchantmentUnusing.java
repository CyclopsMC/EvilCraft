package evilcraft.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
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
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<EnchantmentConfig> eConfig) {
        if(_instance == null)
            _instance = new EnchantmentUnusing(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EnchantmentUnusing getInstance() {
        return _instance;
    }

    private EnchantmentUnusing(ExtendedConfig<EnchantmentConfig> eConfig) {
        super(eConfig, 1, EnumEnchantmentType.ALL);
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
