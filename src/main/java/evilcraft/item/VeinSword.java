package evilcraft.item;

import evilcraft.core.config.configurable.ConfigurableItemSword;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.EnchantmentHelpers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new VeinSword(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static VeinSword getInstance() {
        return _instance;
    }

    private VeinSword(ExtendedConfig<ItemConfig> eConfig) {
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
        EnchantmentHelpers.setEnchantmentLevel(sword, Enchantment.looting, LOOTING_LEVEL);
        return sword;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
    	itemList.add(createCraftingResult());
    }

}
