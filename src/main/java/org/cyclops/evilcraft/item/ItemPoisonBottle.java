package org.cyclops.evilcraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.RegistryEntries;

import javax.annotation.Nullable;

/**
 * Bottle that is retrieved when right-clicking a poison source.
 * @author rubensworks
 *
 */
public class ItemPoisonBottle extends Item {

    public ItemPoisonBottle(Item.Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.addListener(this::onPoisonRightClick);
    }

    public void onPoisonRightClick(PlayerInteractEvent.RightClickBlock event) {
        InteractionHand hand = event.getEntity().getUsedItemHand();
        // Return poison bottle instead of water bottle when right clicking poison fluid source with empty bottle.
        if(hand != null && !event.getEntity().getItemInHand(hand).isEmpty() &&
                event.getEntity().getItemInHand(hand).getItem() == Items.GLASS_BOTTLE) {
            HitResult pos = this.getPlayerPOVHitResult(event.getLevel(), event.getEntity(), ClipContext.Fluid.SOURCE_ONLY);
            if(pos != null && pos.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = BlockPos.containing(pos.getLocation());
                if(event.getLevel().mayInteract(event.getEntity(), blockPos) &&
                        event.getEntity().mayUseItemAt(blockPos, event.getFace(), event.getEntity().getItemInHand(hand)) &&
                        event.getLevel().getBlockState(blockPos).getFluidState().is(Fluids.WATER)) {
                    if(event.getLevel().getFluidState(blockPos).getType() == RegistryEntries.FLUID_POISON) {
                        InventoryHelpers.tryReAddToStack(event.getEntity(), event.getEntity().getItemInHand(hand), new ItemStack(this), hand);
                        event.getLevel().removeBlock(blockPos, false);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        FluidHandlerItemStackSimple.SwapEmpty capabilityProvider = new FluidHandlerItemStackSimple.SwapEmpty(stack, new ItemStack(Items.GLASS_BOTTLE), FluidHelpers.BUCKET_VOLUME);
        capabilityProvider.fill(new FluidStack(RegistryEntries.FLUID_POISON, FluidHelpers.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
        return capabilityProvider;
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements net.minecraft.client.color.item.ItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            return renderPass == 0 ? Helpers.RGBToInt(77, 117, 15) : -1;
        }
    }

}
