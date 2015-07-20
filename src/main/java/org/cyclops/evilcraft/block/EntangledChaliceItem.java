package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.Iterator;

/**
 * Specialized item for the {@link EntangledChalice} blockState.
 * @author rubensworks
 */
public class EntangledChaliceItem extends ItemBlockFluidContainer {

	/**
     * Make a new instance.
     * @param block The blockState instance.
     */
    public EntangledChaliceItem(Block block) {
        super(block);
    }

    /**
     * Get the tank id from the container.
     * @param container The chalice item container.
     * @return The tank id.
     */
    public String getTankID(ItemStack container) {
    	String key = getBlockTank().getTankNBTName();
    	if(container.getTagCompound() == null || !container.getTagCompound().hasKey(key)) {
            // In this case, the tank is invalid!
    		container.setTagCompound(new NBTTagCompound());
            container.getTagCompound().setTag(key, new NBTTagCompound());
    		container.getTagCompound().getCompoundTag(key).setString(WorldSharedTank.NBT_TANKID, "invalid");
    	}
        return container.getTagCompound().getCompoundTag(key).getString(WorldSharedTank.NBT_TANKID);
    }

    /**
     * Set the tank id for the container.
     * @param container The chalice item container.
     * @param tankID The tank id.
     */
    public void setTankID(ItemStack container, String tankID) {
        String key = getBlockTank().getTankNBTName();
        if(container.getTagCompound() == null) {
            container.setTagCompound(new NBTTagCompound());
        }
        if(!container.getTagCompound().hasKey(key)) {
            container.getTagCompound().setTag(key, new NBTTagCompound());
        }
        container.getTagCompound().getCompoundTag(key).setString(WorldSharedTank.NBT_TANKID, tankID);
    }

    /**
     * Set a new unique tank id for the container.
     * @param container The chalice item container.
     */
    public void setNextTankID(ItemStack container) {
        setTankID(container, Integer.toString(EvilCraft.globalCounters.getNext("EntangledChalice")));
    }
    
    @Override
	public FluidStack getFluid(ItemStack container) {
    	return WorldSharedTankCache.getInstance().getTankContent(getTankID(container));
    }
    
    @Override
    protected void setFluid(ItemStack container, FluidStack fluidStack) {
    	WorldSharedTankCache.getInstance().setTankContent(getTankID(container), fluidStack);
    }

    protected void autofill(IFluidContainerItem item, ItemStack itemStack, World world, Entity entity) {
        if(entity instanceof EntityPlayer && !world.isRemote) {
            EntityPlayer player = (EntityPlayer) entity;
            FluidStack tickFluid;
            Iterator<ItemStack> it = new PlayerExtendedInventoryIterator(player);
            do {
                tickFluid = item.getFluid(itemStack);
                ItemStack toFill = it.next();
                ItemHelpers.tryFillContainerForPlayer(item, itemStack, toFill, tickFluid, player);
            } while(tickFluid != null && tickFluid.amount > 0 && it.hasNext());
        }
    }
	
}
