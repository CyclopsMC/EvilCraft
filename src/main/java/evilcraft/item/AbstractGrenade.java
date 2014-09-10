package evilcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Abstract grenade class.
 * @author immortaleeb
 *
 */
public abstract class AbstractGrenade extends ConfigurableItem {

    protected AbstractGrenade(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.maxStackSize = 16;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if(!world.isRemote) {
            if (!entityPlayer.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
            world.playSoundAtEntity(entityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    
            world.spawnEntityInWorld(getThrowableEntity(itemStack, world, entityPlayer));
        }
        return itemStack;
    }
    
    protected abstract EntityThrowable getThrowableEntity(ItemStack itemStack, World world, EntityPlayer player);
}
