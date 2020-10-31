package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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
    protected ThrowableEntity getThrowableEntity(ItemStack itemStack, World world, PlayerEntity player) {
        return new EntityLightningGrenade(world, player);
    }
}
