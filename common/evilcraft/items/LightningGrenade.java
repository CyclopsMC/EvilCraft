package evilcraft.items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
import evilcraft.entities.item.EntityLightningGrenade;

/**
 * Pearl that spawns lightning on collision.
 * @author rubensworks
 *
 */
public class LightningGrenade extends ConfigurableItem {
    
    private static LightningGrenade _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new LightningGrenade(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static LightningGrenade getInstance() {
        return _instance;
    }

    private LightningGrenade(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.maxStackSize = 16;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(!world.isRemote) {
            if (!player.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    
            world.spawnEntityInWorld(new EntityLightningGrenade(world, player));
        }
        return itemStack;
    }

}
