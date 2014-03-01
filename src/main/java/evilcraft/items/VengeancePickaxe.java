package evilcraft.items;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItemPickaxe;

/**
 * A strong pickaxe that may call up spirits.
 * @author rubensworks
 *
 */
public class VengeancePickaxe extends ConfigurableItemPickaxe {
    
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
        boolean result = super.onBlockDestroyed(itemStack, world, block, x, y, z, entity);
        if(result) {
        	if(world.rand.nextInt(VengeancePickaxeConfig.vengeanceChance) == 0) {
        		int area = VengeancePickaxeConfig.areaOfEffect;
        		VengeanceRing.toggleVengeanceArea(world, entity, area, true);
        	}
        }
        return result;
    }

}
