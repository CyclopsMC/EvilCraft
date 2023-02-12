package org.cyclops.evilcraft.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * Abstract grenade class.
 * @author immortaleeb
 *
 */
public abstract class ItemAbstractGrenade extends Item {

    protected ItemAbstractGrenade(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!world.isClientSide()) {
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.MASTER, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));

            ThrowableProjectile entity = getThrowableEntity(itemStack, world, player);
            // MCP: shoot
            entity.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            world.addFreshEntity(entity);
        }
        return MinecraftHelpers.successAction(itemStack);
    }

    protected abstract ThrowableProjectile getThrowableEntity(ItemStack itemStack, Level world, Player player);
}
