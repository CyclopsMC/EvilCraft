package org.cyclops.evilcraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPoisonRightClick(PlayerInteractEvent.RightClickBlock event) {
        Hand hand = event.getPlayer().getActiveHand();
        // Return poison bottle instead of water bottle when right clicking poison fluid source with empty bottle.
        if(hand != null && !event.getPlayer().getHeldItem(hand).isEmpty() &&
                event.getPlayer().getHeldItem(hand).getItem() == Items.GLASS_BOTTLE) {
            RayTraceResult pos = this.rayTrace(event.getWorld(), event.getPlayer(), RayTraceContext.FluidMode.SOURCE_ONLY);
            if(pos != null && pos.getType() == RayTraceResult.Type.BLOCK) {
                BlockPos blockPos = new BlockPos(pos.getHitVec());
                if(event.getWorld().isBlockModifiable(event.getPlayer(), blockPos) &&
                        event.getPlayer().canPlayerEdit(blockPos, event.getFace(), event.getPlayer().getHeldItem(hand)) &&
                        event.getWorld().getBlockState(blockPos).getMaterial() == Material.WATER) {
                    if(event.getWorld().getFluidState(blockPos).getFluid() == RegistryEntries.FLUID_POISON) {
                        InventoryHelpers.tryReAddToStack(event.getPlayer(), event.getPlayer().getHeldItem(hand), new ItemStack(this), hand);
                        event.getWorld().removeBlock(blockPos, false);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        FluidHandlerItemStackSimple.SwapEmpty capabilityProvider = new FluidHandlerItemStackSimple.SwapEmpty(stack, new ItemStack(Items.GLASS_BOTTLE), FluidHelpers.BUCKET_VOLUME);
        capabilityProvider.fill(new FluidStack(RegistryEntries.FLUID_POISON, FluidHelpers.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
        return capabilityProvider;
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements IItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            return renderPass == 0 ? Helpers.RGBToInt(77, 117, 15) : -1;
        }
    }

}
