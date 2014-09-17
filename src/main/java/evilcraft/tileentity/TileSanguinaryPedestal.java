package evilcraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import evilcraft.api.ILocation;
import evilcraft.block.BloodStainedBlock;
import evilcraft.block.PurifierConfig;
import evilcraft.block.SanguinaryPedestal;
import evilcraft.block.SanguinaryPedestalConfig;
import evilcraft.core.algorithm.Location;
import evilcraft.core.algorithm.RegionIterator;
import evilcraft.core.helper.LocationHelpers;
import evilcraft.core.tileentity.TankInventoryTileEntity;
import evilcraft.fluid.Blood;
import evilcraft.item.BloodExtractorConfig;

/**
 * Tile for the {@link SanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class TileSanguinaryPedestal extends TankInventoryTileEntity {
    
    /**
     * The fluid it uses.
     */
    public static final Fluid FLUID = Blood.getInstance();
    
    private static final int MB_RATE = 10;
    private static final int TANK_MULTIPLIER = 10;
    private static final int OFFSET = 2;
    
    private RegionIterator regionIterator;
    
    /**
     * Make a new instance.
     */
    public TileSanguinaryPedestal() {
        super(0, PurifierConfig._instance.getNamedId(), 1, FluidContainerRegistry.BUCKET_VOLUME * BloodExtractorConfig.maxMB 
        		* TANK_MULTIPLIER, SanguinaryPedestalConfig._instance.getNamedId() + "tank", FLUID);
    }
    
    @Override
    public void updateTileEntity() {
    	super.updateTileEntity();
    	
    	if(!getWorldObj().isRemote && !getTank().isFull()) {
	    	// Drain next block in tick
	    	ILocation location = getNextLocation();
	    	Block block = LocationHelpers.getBlock(getWorldObj(), location);
	    	if(block == BloodStainedBlock.getInstance()) {
	    		int metaData = LocationHelpers.getBlockMeta(getWorldObj(), location);
	    		LocationHelpers.setBlock(getWorldObj(), location, BloodStainedBlock.getInstance().getBlockFromMetadata(metaData));
	    		if(!getTank().isFull()) {
	    			fill(new FluidStack(FLUID, SanguinaryPedestalConfig.extractMB), true);
	    		}
	    	}
	    	
	    	// Auto-drain the inner tank
	    	if(!getTank().isEmpty()) {
				for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
					TileEntity tile = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
					if(!getTank().isEmpty() && tile instanceof IFluidHandler) {
						IFluidHandler handler = (IFluidHandler) tile;
						FluidStack fluidStack = new FluidStack(getTank().getFluidType(), MB_RATE);
						if(handler.canFill(direction.getOpposite(), getTank().getFluidType())
								&& handler.fill(direction.getOpposite(), fluidStack, false) > 0) {
							int filled = handler.fill(direction.getOpposite(), fluidStack, true);
							drain(filled, true);
						}
					}
				}
			}
    	}
    	
    }

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return false;
	}
	
	@Override
    public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}
	
	private ILocation getNextLocation() {
		if(regionIterator == null) {
			regionIterator = new RegionIterator(new Location(xCoord, yCoord, zCoord), OFFSET, true);
		}
		return regionIterator.next();
	}

}
