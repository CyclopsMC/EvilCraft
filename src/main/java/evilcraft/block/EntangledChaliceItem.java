package evilcraft.block;

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
    		// TODO: make a NEW ID!
    		container.stackTagCompound = new NBTTagCompound();
    		container.stackTagCompound.setString(WorldSharedTank.NBT_TANKID, "TODO"); // TODO!
    	}
    	return container.stackTagCompound.getCompoundTag(key).getString(WorldSharedTank.NBT_TANKID);
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
