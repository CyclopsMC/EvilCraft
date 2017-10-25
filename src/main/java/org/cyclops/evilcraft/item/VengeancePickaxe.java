package org.cyclops.evilcraft.item;

import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableItemPickaxe;
import org.cyclops.evilcraft.enchantment.EnchantmentVengeance;

import java.util.Map;

/**
 * A strong pickaxe that may call up spirits.
 * @author rubensworks
 *
 */
public class VengeancePickaxe extends ConfigurableItemPickaxe {
	
	/**
	 * The fortune level of this pickaxe.
	 */
	public static final int FORTUNE_LEVEL = 5;
    /**
     * The vengeance level of this pickaxe.
     */
    public static final int VENGEANCE_LEVEL = 3;
    
    private static VengeancePickaxe _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static VengeancePickaxe getInstance() {
        return _instance;
    }

    public VengeancePickaxe(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, ToolMaterial.DIAMOND);
        this.setMaxDamage(154);
        this.efficiency *= 1.250F;
    }

    // Can break all blocks, like diamond
    @Override
    public boolean canHarvestBlock(IBlockState blockState) {
        return true;
    }
    
    /**
     * Get the crafting result of this pickaxe.
     * It has fortune X by default.
     * @return The pickaxe with enchantment.
     */
    public static ItemStack createCraftingResult() {
    	ItemStack pickaxe = new ItemStack(VengeancePickaxe.getInstance());
        Map<Enchantment, Integer> enchantments = Maps.newHashMap();
        enchantments.put(Enchantments.FORTUNE, FORTUNE_LEVEL);
        enchantments.put(EnchantmentVengeance.getInstance(), VENGEANCE_LEVEL);
        EnchantmentHelper.setEnchantments(enchantments, pickaxe);
        return pickaxe;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @Override
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (!ItemStackHelpers.isValidCreativeTab(this, creativeTabs)) return;
    	list.add(createCraftingResult());
    }

}
