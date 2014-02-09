package evilcraft.items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.api.Helpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
import evilcraft.entities.monster.WerewolfConfig;

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
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {           
        if(!world.isRemote) {
            stack.stackSize--;
            Helpers.spawnCreature(world, WerewolfConfig._instance.ID, x, y + 1, z);
            return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
        return false;
    }

}
