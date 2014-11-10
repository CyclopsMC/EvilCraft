package evilcraft.block;

import evilcraft.core.world.GlobalCounter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.core.fluid.WorldSharedTank;
import evilcraft.core.fluid.WorldSharedTankCache;
import evilcraft.core.item.ItemBlockFluidContainer;

/**
 * Specialized item for the {@link EntangledChalice} block.
 * @author rubensworks
 */
public class EntangledChaliceItem extends ItemBlockFluidContainer {

	/**
     * Make a new instance.
     * @param block The block instance.
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
    	if(container.stackTagCompound == null || !container.stackTagCompound.hasKey(key)) {
            // In this case, the tank is invalid!
    		container.stackTagCompound = new NBTTagCompound();
            container.stackTagCompound.setTag(key, new NBTTagCompound());
    		container.stackTagCompound.getCompoundTag(key).setString(WorldSharedTank.NBT_TANKID, "invalid");
    	}
        return container.stackTagCompound.getCompoundTag(key).getString(WorldSharedTank.NBT_TANKID);
    }

    /**
     * Set the tank id for the container.
     * @param container The chalice item container.
     * @param tankID The tank id.
     */
    public void setTankID(ItemStack container, String tankID) {
        String key = getBlockTank().getTankNBTName();
        if(container.stackTagCompound == null) {
            container.stackTagCompound = new NBTTagCompound();
        }
        if(!container.stackTagCompound.hasKey(key)) {
            container.stackTagCompound.setTag(key, new NBTTagCompound());
        }
        container.stackTagCompound.getCompoundTag(key).setString(WorldSharedTank.NBT_TANKID, tankID);
    }

    /**
     * Set a new unique tank id for the container.
     * @param container The chalice item container.
     */
    public void setNextTankID(ItemStack container) {
        setTankID(container, Integer.toString(GlobalCounter.getInstance().getNext("EntangledChalice")));
    }
    
    @Override
	public FluidStack getFluid(ItemStack container) {
    	return WorldSharedTankCache.getInstance().getTankContent(getTankID(container));
    }
    
    @Override
    protected void setFluid(ItemStack container, FluidStack fluidStack) {
    	WorldSharedTankCache.getInstance().setTankContent(getTankID(container), fluidStack);
    }
	
}
