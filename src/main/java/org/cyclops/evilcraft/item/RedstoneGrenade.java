package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.entity.item.EntityRedstoneGrenade;

/**
 * Grenade that will trigger a redstone signal.
 * @author immortaleeb
 *
 */
public class RedstoneGrenade extends AbstractGrenade {
    
    private static RedstoneGrenade _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static RedstoneGrenade getInstance() {
        return _instance;
    }

    public RedstoneGrenade(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

    @Override
    protected EntityThrowable getThrowableEntity(ItemStack itemStack, World world, EntityPlayer player) {
        return new EntityRedstoneGrenade(world, player);
    }

}
