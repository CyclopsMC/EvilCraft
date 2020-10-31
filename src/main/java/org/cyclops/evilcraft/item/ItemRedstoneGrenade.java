package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.evilcraft.entity.item.EntityRedstoneGrenade;

/**
 * Grenade that will trigger a redstone signal.
 * @author immortaleeb
 *
 */
public class ItemRedstoneGrenade extends ItemAbstractGrenade {

    public ItemRedstoneGrenade(Properties properties) {
        super(properties);
    }

    @Override
    protected ThrowableEntity getThrowableEntity(ItemStack itemStack, World world, PlayerEntity player) {
        return new EntityRedstoneGrenade(world, player);
    }

}
