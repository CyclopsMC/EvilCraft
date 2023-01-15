package org.cyclops.evilcraft.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    protected ThrowableProjectile getThrowableEntity(ItemStack itemStack, Level world, Player player) {
        return new EntityRedstoneGrenade(world, player);
    }

}
