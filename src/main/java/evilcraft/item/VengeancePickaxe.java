package evilcraft.item;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableItemPickaxe;
import evilcraft.core.helper.EnchantmentHelpers;

/**
 * A strong pickaxe that may call up spirits.
 * @author rubensworks
 *
 */
public class VengeancePickaxe extends ConfigurableItemPickaxe {
	
	/**
	 * The fortune level of this pickaxe.
	 */
	public static final int FORTUNE_LEVEL = 10;
    
    private static VengeancePickaxe _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new VengeancePickaxe(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static VengeancePickaxe getInstance() {
        return _instance;
    }

    private VengeancePickaxe(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, Item.ToolMaterial.EMERALD);
        this.efficiencyOnProperMaterial *= 1.250F;
    }
    
    // Can break all blocks, like diamond
    @Override
    public boolean func_150897_b(Block block) {
        return true;
    }
    
    @Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if(!world.isRemote) {
    	boolean result = super.onBlockDestroyed(itemStack, world, block, x, y, z, entity);
        if(result) {
        	if(world.rand.nextInt(VengeancePickaxeConfig.vengeanceChance) == 0) {
        		int area = VengeancePickaxeConfig.areaOfEffect;
        		VengeanceRing.toggleVengeanceArea(world, entity, area, true, true, true);
        	}
        }
        return result;
        }
        return super.onBlockDestroyed(itemStack, world, block, x, y, z, entity);
    }
    
    /**
     * Get the crafting result of this pickaxe.
     * It has fortune X by default.
     * @return The pickaxe with enchantment.
     */
    public static ItemStack createCraftingResult() {
    	ItemStack pickaxe = new ItemStack(VengeancePickaxe.getInstance());
        EnchantmentHelpers.setEnchantmentLevel(pickaxe, Enchantment.fortune, FORTUNE_LEVEL);
        return pickaxe;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
    	itemList.add(createCraftingResult());
    }

}
