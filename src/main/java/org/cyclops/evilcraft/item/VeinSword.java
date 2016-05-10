package org.cyclops.evilcraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableItemSword;

import java.util.List;

/**
 * A strong pickaxe that may call up spirits.
 * @author rubensworks
 *
 */
public class VeinSword extends ConfigurableItemSword {
    
    private static VeinSword _instance = null;
    
    /**
	 * The looting level of this sword.
	 */
	public static final int LOOTING_LEVEL = 2;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static VeinSword getInstance() {
        return _instance;
    }

    public VeinSword(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, Item.ToolMaterial.GOLD);
        this.setMaxDamage(VeinSwordConfig.durability);
    }
    
    /**
     * Get the crafting result of this pickaxe.
     * It has fortune X by default.
     * @return The pickaxe with enchantment.
     */
    public static ItemStack createCraftingResult() {
    	ItemStack sword = new ItemStack(VeinSword.getInstance());
        EnchantmentHelpers.setEnchantmentLevel(sword, Enchantments.looting, LOOTING_LEVEL);
        return sword;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
    	itemList.add(createCraftingResult());
    }

}
