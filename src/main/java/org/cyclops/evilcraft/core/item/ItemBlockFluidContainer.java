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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.item.ItemBlockNBT;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.helper.ItemHelpers;

import java.util.List;

/**
 * {@link ItemBlock} that can be used for blocks that have a tile entity with a fluid container.
 * The blockState must implement {@link IBlockTank}.
 * Instances of this will also keep it's tank capacity next to the contents.
 * @author rubensworks
 *
 */
public class ItemBlockFluidContainer extends ItemBlockNBT implements IFluidContainerItem {
    
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
    
    /**
     * @return The blockState tank.
     */
    public IBlockTank getBlockTank() {
    	return block;
    }

	@Override
	public FluidStack getFluid(ItemStack container) {
		String key = block.getTankNBTName();
		if (container.getTagCompound() == null || !container.getTagCompound().hasKey(key)) {
            return null;
        }
        return FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag(key));
	}

	protected void setFluid(ItemStack container, FluidStack fluidStack) {
		String key = block.getTankNBTName();
		if (fluidStack == null || fluidStack.amount <= 0) {
            container.getTagCompound().removeTag(key);
            if (container.getTagCompound().hasNoTags()) {
                container.setTagCompound(null);
            }
        } else {
            if(container.getTagCompound() == null) {
                container.setTagCompound(new NBTTagCompound());
            }
	        NBTTagCompound fluidTag = container.getTagCompound().getCompoundTag(key);
	        fluidStack.writeToNBT(fluidTag);
            fluidTag.removeTag("Empty"); // Make sure that after this blockState is placed the fluid inside is correctly recognised.
	        container.getTagCompound().setTag(key, fluidTag);
        }
	}
	
	@Override
	public int getCapacity(ItemStack container) {
		return block.getTankCapacity(container);
	}
	
	/**
     * Set the maximal tank capacity.
     * @param container The item stack.
     * @param capacity The maximal tank capacity in mB.
     */
	public void setCapacity(ItemStack container, int capacity) {
		block.setTankCapacity(container, capacity);
	}
	
	@Override
	protected void readAdditionalInfo(TileEntity tile, ItemStack itemStack) {
		super.readAdditionalInfo(tile, itemStack);
		if(tile instanceof TankInventoryTileEntity) {
			TankInventoryTileEntity tankTile = (TankInventoryTileEntity) tile;
			tankTile.getTank().setCapacity(getCapacity(itemStack));
		}
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {
		int capacity = getCapacity(container);
		
		if(resource == null) {
            return 0;
        }
		
		FluidStack stack = getFluid(container);

        if(!doFill) {
            if(stack == null) {
                return Math.min(capacity, resource.amount);
            }

            if(!stack.isFluidEqual(resource)) {
                return 0;
            }

            return Math.min(capacity - stack.amount, resource.amount);
        }
        
        if(stack == null) {
        	stack = resource.copy();
        	stack.amount = 0;
        }

        if(!resource.isFluidEqual(stack)) {
            return 0;
        }

        int filled = capacity - stack.amount;
        if(resource.amount < filled) {
            stack.amount = Helpers.addSafe(stack.amount, resource.amount);
            filled = resource.amount;
        } else {
            stack.amount = capacity;
        }

        setFluid(container, stack);
        return filled;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
		FluidStack stack = getFluid(container);
		
		if(stack == null) {
			return null;
		}

        int currentAmount = stack.amount;
        stack.amount = Math.min(stack.amount, maxDrain);
        if(doDrain) {
        	FluidStack newStack = stack.copy();
        	newStack.amount = currentAmount - stack.amount;
            setFluid(container, newStack);
        }
        return stack;
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if(block.isActivatable()) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, block.toggleActivation(itemStack, world, player));
        }
        return super.onItemRightClick(itemStack, world, player, hand);
    }

    protected void autofill(IFluidContainerItem item, ItemStack itemStack, World world, Entity entity) {
        ItemHelpers.updateAutoFill(this, itemStack, world, entity);
    }
	
	@Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
    	if(block.isActivatable() && block.isActivated(itemStack, world, entity)) {
            autofill(this, itemStack, world, entity);
    	}
        super.onUpdate(itemStack, world, entity, par4, par5);
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        if(block.isActivatable()) {
	        L10NHelpers.addStatusInfo(list, block.isActivated(itemStack, entityPlayer.worldObj, entityPlayer),
                    getUnlocalizedName() + ".info.autoSupply");
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }
}
