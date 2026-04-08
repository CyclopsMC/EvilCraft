package org.cyclops.evilcraft.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * A base focus item.
 * @author rubensworks
 *
 */
public abstract class ItemAbstractFocus extends Item {

    private static final int TICK_MODULUS = 3;

    public ItemAbstractFocus(Properties properties) {
        super(properties);
    }

    private int getItemInUseDuration(LivingEntity player) {
        return Math.max(0, player.getTicksUsingItem() - player.getUseItemRemainingTicks());
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(getItemInUseDuration(player) > 0) {
            return InteractionResult.FAIL;
        } else {
            player.startUsingItem(hand);
        }
        return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity entity) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level world, LivingEntity player, int duration) {
        if(getItemInUseDuration(player) > 6) {
            // Play stop sound
            if (player.level().isClientSide()) {
                player.playSound(RegistryEntries.SOUNDEVENT_EFFECT_VENGEANCEBEAM_STOP.get(), 0.6F + player.level().getRandom().nextFloat() * 0.2F, 1.0F);
            }
            return true;
        }
        return false;
    }

    protected abstract ThrowableProjectile newBeamEntity(LivingEntity player);

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack itemStack, int remaining) {
        int duration = getUseDuration(itemStack, player) - remaining;
        if(duration > 6) {
            if(IModHelpers.get().getWorldHelpers().efficientTick(player.level(), TICK_MODULUS, player.getId())) {
                ThrowableProjectile beam = newBeamEntity(player);
                if(!player.level().isClientSide()) {
                    // Last three params: pitch offset, velocity, inaccuracy
                    // MCP: shoot
                    beam.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, 0.5F, 1.0F);
                    player.level().addFreshEntity(beam);
                }
            }
        } else {
            if(duration == 3 && player.level().isClientSide()) {
                // Play start sound
                player.playSound(RegistryEntries.SOUNDEVENT_EFFECT_VENGEANCEBEAM_START.get(),  0.6F + player.level().getRandom().nextFloat() * 0.2F, 1.0F);
            }
        }
    }

}
