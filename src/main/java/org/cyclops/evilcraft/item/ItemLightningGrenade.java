package org.cyclops.evilcraft.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.entity.item.EntityLightningGrenade;

/**
 * Pearl that spawns lightning on collision.
 * @author rubensworks
 *
 */
public class ItemLightningGrenade extends ItemAbstractGrenade {

    public ItemLightningGrenade(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected ThrowableProjectile getThrowableEntity(ItemStack itemStack, Level world, Player player) {
        return new EntityLightningGrenade(world, player);
    }
}
