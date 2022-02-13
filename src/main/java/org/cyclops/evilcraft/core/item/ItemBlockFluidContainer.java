package org.cyclops.evilcraft.core.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
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

import net.minecraft.item.Item.Properties;

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
	protected boolean itemStackDataToTile(ItemStack itemStack, TileEntity tile) {
        tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                .ifPresent(fluidHandlerTile -> {
                    itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
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
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if(block.isActivatable()) {
            return new ActionResult<ItemStack>(ActionResultType.PASS, block.toggleActivation(player.getItemInHand(hand), world, player));
        }
        return super.use(world, player, hand);
    }

    protected void autofill(int itemSlot, IFluidHandlerItem source, World world, Entity entity) {
        ItemHelpers.updateAutoFill(source, world, entity, BlockDarkTankConfig.autoFillBuckets);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(block.isActivatable() && block.isActivated(stack, worldIn)) {
            FluidUtil.getFluidHandler(stack)
                    .ifPresent(fluidHandler -> autofill(itemSlot, fluidHandler, worldIn, entityIn));
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
        if (CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY != null) {
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
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new FluidHandlerItemCapacity(stack, block.getDefaultCapacity());
    }
}
