package evilcraft.items;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableItem;

/**
 * A bone that is dropped from werewolves.
 * @author rubensworks
 *
 */
public class WerewolfBone extends ConfigurableItem {
    
    private static WerewolfBone _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new WerewolfBone(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static WerewolfBone getInstance() {
        return _instance;
    }

    private WerewolfBone(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }
    
    // Disabled for now, add check for isEnabled Werewolf if this will be enabled again.
    /*@Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {           
        if(!world.isRemote) {
            stack.stackSize--;
            Helpers.spawnCreature(world, WerewolfConfig._instance.ID, x, y + 1, z);
            return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
        return false;
    }*/

}
