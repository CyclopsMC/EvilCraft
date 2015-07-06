package evilcraft.item;

import evilcraft.core.config.configurable.ConfigurableItemPickaxe;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.EnchantmentHelpers;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

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
        this.setMaxDamage(154);
        this.efficiencyOnProperMaterial *= 1.250F;
    }

    // Can break all blocks, like diamond
    @Override
    public boolean canHarvestBlock(Block block) {
        return true;
    }
    
    @Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, BlockPos blockPos, EntityLivingBase entity) {
        if(!world.isRemote) {
    	boolean result = super.onBlockDestroyed(itemStack, world, block, blockPos, entity);
        if(result) {
            int chance = VengeancePickaxeConfig.vengeanceChance;
        	if(chance > 0 && world.rand.nextInt(chance) == 0) {
        		int area = VengeancePickaxeConfig.areaOfEffect;
        		VengeanceRing.toggleVengeanceArea(world, entity, area, true, true, true);
        	}
        }
        return result;
        }
        return super.onBlockDestroyed(itemStack, world, block, blockPos, entity);
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
