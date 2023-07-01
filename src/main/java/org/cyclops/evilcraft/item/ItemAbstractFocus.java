package org.cyclops.evilcraft.item;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.EvilCraftSoundEvents;

/**
 * A base focus item.
 * @author rubensworks
 *
 */
public abstract class ItemAbstractFocus extends Item {

    private static final int TICK_MODULUS = 3;

    public ItemAbstractFocus(Properties properties) {
        super(properties);
        if (MinecraftHelpers.isClientSide()) {
            registerProperties();
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void registerProperties() {
        ItemProperties.register(this, new ResourceLocation("pull"), new ItemPropertyFunction() {
            @OnlyIn(Dist.CLIENT)
            public float call(ItemStack stack, ClientLevel worldIn, LivingEntity entityIn, int id) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    ItemStack itemStack = entityIn.getUseItem();
                    return !itemStack.isEmpty() && itemStack.getItem() instanceof ItemAbstractFocus ? (float)(stack.getUseDuration() - entityIn.getUseItemRemainingTicks()) / 20.0F : 0.0F;
                }
            }
        });
        ItemProperties.register(this, new ResourceLocation("pulling"), new ItemPropertyFunction() {
            @OnlyIn(Dist.CLIENT)
            public float call(ItemStack stack, ClientLevel worldIn, LivingEntity entityIn, int id) {
                return entityIn != null && entityIn.isUsingItem() && entityIn.getUseItem() == stack ? 1.0F : 0.0F;
            }
        });
    }

    private int getItemInUseDuration(LivingEntity player) {
        return Math.max(0, player.getTicksUsingItem() - player.getUseItemRemainingTicks());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(getItemInUseDuration(player) > 0) {
            return new InteractionResultHolder<ItemStack>(InteractionResult.FAIL, itemStack);
        } else {
            player.startUsingItem(hand);
        }
        return MinecraftHelpers.successAction(itemStack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level world, LivingEntity player, int duration) {
        if(player.level().isClientSide() && getItemInUseDuration(player) > 6) {
            // Play stop sound
            player.playSound(EvilCraftSoundEvents.effect_vengeancebeam_stop, 0.6F + player.level().random.nextFloat() * 0.2F, 1.0F);
        }
    }

    protected abstract ThrowableProjectile newBeamEntity(LivingEntity player);

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack itemStack, int remaining) {
        int duration = getUseDuration(itemStack) - remaining;
        if(duration > 6) {
            if(WorldHelpers.efficientTick(player.level(), TICK_MODULUS, player.getId())) {
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
                player.playSound(EvilCraftSoundEvents.effect_vengeancebeam_start,  0.6F + player.level().random.nextFloat() * 0.2F, 1.0F);
            }
        }
    }

}
