package org.cyclops.evilcraft.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
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
    public int getUseDuration(ItemStack itemStack, LivingEntity entity) {
        return 32;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.EAT;
    }

    protected boolean canEat(ItemStack itemStack) {
        FluidStack fluidStack = FluidUtil.getFirstStackContained(itemStack);
        return !fluidStack.isEmpty() && fluidStack.getAmount() >= ItemRejuvenatedFleshConfig.biteUsage;
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(canEat(itemStack) && player.canEat(false)) {
            player.startUsingItem(hand);
            return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity entity) {
        try (var tx = Transaction.openRoot()) {
            itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack))
                    .extract(FluidResource.of(getFluid()), ItemRejuvenatedFleshConfig.biteUsage, tx);
            tx.commit();
        }
        if(entity instanceof Player) {
            ((Player) entity).getFoodData().eat(3, 0.5F);
        }
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        return itemStack;
    }

}
