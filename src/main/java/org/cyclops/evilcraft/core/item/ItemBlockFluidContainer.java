package org.cyclops.evilcraft.core.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.item.ItemBlockNBT;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;
import org.cyclops.evilcraft.core.helper.ItemHelpers;

import java.util.List;

/**
 * {@link ItemBlock} that can be used for blocks that have a tile entity with a fluid container.
 * The blockState must implement {@link IBlockTank}.
 * Instances of this will also keep it's tank capacity next to the contents.
 * @author rubensworks
 *
 */
public class ItemBlockFluidContainer extends ItemBlockNBT {
    
	private IBlockTank block;
	
    /**
     * Make a new instance.
     * @param block The blockState instance.
     */
    public ItemBlockFluidContainer(Block block) {
        super(block);
        this.setHasSubtypes(false);
        // Will crash if no valid instance of.
        this.block = (IBlockTank) block;
    }

	@Override
	protected void itemStackDataToTile(ItemStack itemStack, TileEntity tile) {
        BlockTankHelpers.itemStackDataToTile(itemStack, tile);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if(block.isActivatable()) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, block.toggleActivation(player.getHeldItem(hand), world, player));
        }
        return super.onItemRightClick(world, player, hand);
    }

    protected void autofill(int itemSlot, IFluidHandlerItem source, World world, Entity entity) {
        ItemHelpers.updateAutoFill(source, world, entity);
    }
	
	@Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean par5) {
    	if(block.isActivatable() && block.isActivated(itemStack, world, entity)) {
            autofill(itemSlot, FluidUtil.getFluidHandler(itemStack), world, entity);
    	}
        super.onUpdate(itemStack, world, entity, itemSlot, par5);
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        if(block.isActivatable()) {
	        L10NHelpers.addStatusInfo(list, block.isActivated(itemStack, entityPlayer.world, entityPlayer),
                    getUnlocalizedName() + ".info.auto_supply");
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem() || oldStack.getMetadata() != newStack.getMetadata();
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidHandlerItemCapacity(stack, block.getDefaultCapacity());
    }
}
