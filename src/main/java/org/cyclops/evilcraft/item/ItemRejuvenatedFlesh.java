package org.cyclops.evilcraft.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
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
    public UseAction getUseAction(ItemStack itemStack) {
        return UseAction.EAT;
    }

    protected boolean canEat(ItemStack itemStack) {
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
        return !fluidStack.isEmpty() && fluidStack.getAmount() >= ItemRejuvenatedFleshConfig.biteUsage;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(canEat(itemStack) && player.canEat(false)) {
            player.setActiveHand(hand);
            return MinecraftHelpers.successAction(itemStack);
        }
        return new ActionResult<>(ActionResultType.FAIL, itemStack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, LivingEntity entity) {
        FluidUtil.getFluidHandler(itemStack).orElseGet(null)
                .drain(ItemRejuvenatedFleshConfig.biteUsage, IFluidHandler.FluidAction.EXECUTE);
        if(entity instanceof PlayerEntity) {
            ((PlayerEntity) entity).getFoodStats().addStats(3, 0.5F);
        }
        world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        return itemStack;
    }

}
