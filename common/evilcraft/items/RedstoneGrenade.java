package evilcraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.entities.item.EntityRedstoneGrenade;

public class RedstoneGrenade extends AbstractGrenade {
    
    private static RedstoneGrenade _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new RedstoneGrenade(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static RedstoneGrenade getInstance() {
        return _instance;
    }

    protected RedstoneGrenade(ExtendedConfig eConfig) {
        super(eConfig);
    }

    @Override
    protected EntityThrowable getThrowableEntity(ItemStack itemStack, World world, EntityPlayer player) {
        return new EntityRedstoneGrenade(world, player);
    }

}
