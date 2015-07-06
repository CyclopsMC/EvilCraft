package evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import evilcraft.entity.item.EntityRedstoneGrenade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Grenade that will trigger a redstone signal.
 * @author immortaleeb
 *
 */
public class RedstoneGrenade extends AbstractGrenade {
    
    private static RedstoneGrenade _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new RedstoneGrenade(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static RedstoneGrenade getInstance() {
        return _instance;
    }

    protected RedstoneGrenade(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

    @Override
    protected EntityThrowable getThrowableEntity(ItemStack itemStack, World world, EntityPlayer player) {
        return new EntityRedstoneGrenade(world, player);
    }

}
