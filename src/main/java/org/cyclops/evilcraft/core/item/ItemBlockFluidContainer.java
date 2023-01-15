package org.cyclops.evilcraft.core.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerMutable;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.item.ItemBlockNBT;
import org.cyclops.evilcraft.block.BlockDarkTankConfig;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;
import org.cyclops.evilcraft.core.helper.ItemHelpers;

import javax.annotation.Nullable;
import java.util.List;

/**
 * {@link BlockItem} that can be used for blocks that have a tile entity with a fluid container.
 * The blockState must implement {@link IBlockTank}.
 * Instances of this will also keep it's tank capacity next to the contents.
 * @author rubensworks
 *
 */
public class ItemBlockFluidContainer extends ItemBlockNBT {

    private IBlockTank block;

    public ItemBlockFluidContainer(Block block, Properties builder) {
        super(block, builder);
        // Will crash if no valid instance of.
        this.block = (IBlockTank) block;
    }

    @Override
    public boolean isFoil(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }

    @Override
    protected boolean itemStackDataToTile(ItemStack itemStack, BlockEntity tile) {
        tile.getCapability(ForgeCapabilities.FLUID_HANDLER)
                .ifPresent(fluidHandlerTile -> {
                    itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                            .ifPresent(fluidHandlerItem -> {
                                if (fluidHandlerTile instanceof IFluidHandlerMutable) {
                                    ((IFluidHandlerMutable) fluidHandlerTile).setFluidInTank(0, fluidHandlerItem.getFluidInTank(0));
                                }
                                if (fluidHandlerTile instanceof IFluidHandlerCapacity) {
                                    ((IFluidHandlerCapacity) fluidHandlerTile).setTankCapacity(0, fluidHandlerItem.getTankCapacity(0));
                                }
                            });
                });
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(block.isActivatable()) {
            return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, block.toggleActivation(player.getItemInHand(hand), world, player));
        }
        return super.use(world, player, hand);
    }

    protected void autofill(int itemSlot, IFluidHandlerItem source, Level world, Entity entity) {
        ItemHelpers.updateAutoFill(source, world, entity, BlockDarkTankConfig.autoFillBuckets);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(block.isActivatable() && block.isActivated(stack, worldIn)) {
            FluidUtil.getFluidHandler(stack)
                    .ifPresent(fluidHandler -> autofill(itemSlot, fluidHandler, worldIn, entityIn));
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
        if (ForgeCapabilities.FLUID_HANDLER != null) {
            list.add(BlockTankHelpers.getInfoTank(itemStack));
        }
        if(block.isActivatable()) {
            L10NHelpers.addStatusInfo(list, block.isActivated(itemStack, world),
                    getDescriptionId() + ".info.auto_supply");
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem() || slotChanged;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new FluidHandlerItemCapacity(stack, block.getDefaultCapacity());
    }
}
