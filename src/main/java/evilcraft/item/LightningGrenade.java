package evilcraft.item;

import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.entity.item.EntityLightningGrenade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Pearl that spawns lightning on collision.
 * @author rubensworks
 *
 */
public class LightningGrenade extends AbstractGrenade {
    
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
    }

    @Override
    protected EntityThrowable getThrowableEntity(ItemStack itemStack, World world, EntityPlayer player) {
        return new EntityLightningGrenade(world, player);
    }
}
