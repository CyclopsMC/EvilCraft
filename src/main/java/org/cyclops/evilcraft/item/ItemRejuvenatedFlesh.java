package org.cyclops.evilcraft.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;

/**
 * Flesh which can be infinitely eaten, consuming Blood per bite.
 * @author rubensworks
 *
 */
public class ItemRejuvenatedFlesh extends ItemBloodContainer {

    public ItemRejuvenatedFlesh(Item.Properties properties) {
        super(properties, ItemRejuvenatedFleshConfig.containerSize);
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return Rarity.RARE;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.EAT;
    }

    protected boolean canEat(ItemStack itemStack) {
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
        return !fluidStack.isEmpty() && fluidStack.getAmount() >= ItemRejuvenatedFleshConfig.biteUsage;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(canEat(itemStack) && player.canEat(false)) {
            player.startUsingItem(hand);
            return MinecraftHelpers.successAction(itemStack);
        }
        return new InteractionResultHolder<>(InteractionResult.FAIL, itemStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity entity) {
        FluidUtil.getFluidHandler(itemStack).orElseGet(null)
                .drain(ItemRejuvenatedFleshConfig.biteUsage, IFluidHandler.FluidAction.EXECUTE);
        if(entity instanceof Player) {
            ((Player) entity).getFoodData().eat(3, 0.5F);
        }
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        return itemStack;
    }

}
