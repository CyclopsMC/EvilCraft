package evilcraft.items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
import evilcraft.entities.item.EntityBroom;

public class Broom extends ConfigurableItem {
    
    private static Broom _instance = null;
    
    private static final float Y_SPAWN_OFFSET = 1.5f;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new Broom(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static Broom getInstance() {
        return _instance;
    }

    private Broom(ExtendedConfig eConfig) {
        super(eConfig);
        this.maxStackSize = 1;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    	
    	if (!world.isRemote) {
    		world.spawnEntityInWorld(new EntityBroom(world, x + 0.5, y + Y_SPAWN_OFFSET, z + 0.5));
    		return true;
    	}
    	
    	return false;
    }
    
}
