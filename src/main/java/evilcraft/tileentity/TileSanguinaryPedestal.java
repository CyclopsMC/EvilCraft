package evilcraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
import evilcraft.network.PacketHandler;
import evilcraft.network.packet.SanguinaryPedestalBlockReplacePacket;

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
    
    private static final int MB_RATE = 100;
    private static final int TANK_BUCKETS = 10;
    private static final int OFFSET = 2;
    private static final int OFFSET_EFFICIENCY = 4;
    private static final int ACTIONS_PER_TICK_EFFICIENCY = 5;
    
    private RegionIterator regionIterator;
    
    /**
     * Make a new instance.
     */
    public TileSanguinaryPedestal() {
        super(0, PurifierConfig._instance.getNamedId(), 1, FluidContainerRegistry.BUCKET_VOLUME * TANK_BUCKETS,
        		SanguinaryPedestalConfig._instance.getNamedId() + "tank", FLUID);
    }
    
    protected void afterBlockReplace(World world, ILocation location, Block block, int amount) {
    	// NOTE: this is only called server-side, so make sure to send packets where needed.
    	
    	// Fill tank
    	if(!getTank().isFull()) {
			fill(new FluidStack(FLUID, amount), true);
		}
    	
    	PacketHandler.sendToServer(new SanguinaryPedestalBlockReplacePacket(location, block));
    }

    protected boolean hasEfficiency() {
        return getBlockMetadata() == 1;
    }
    
    @Override
    public void updateTileEntity() {
    	super.updateTileEntity();

        if(!getWorldObj().isRemote) {
            int actions = hasEfficiency() ? ACTIONS_PER_TICK_EFFICIENCY : 1;
	    	// Drain next block in tick
    		while(!getTank().isFull() && actions > 0) {
		    	ILocation location = getNextLocation();
		    	Block block = LocationHelpers.getBlock(getWorldObj(), location);
		    	if(block == BloodStainedBlock.getInstance()) {
		    		BloodStainedBlock.UnstainResult result = BloodStainedBlock.getInstance().unstainBlock(getWorldObj(),
		    				location, getTank().getCapacity() - getTank().getFluidAmount());
		    		if(result.amount > 0) {
                        if(hasEfficiency()) {
                            result.amount *= SanguinaryPedestalConfig.efficiencyBoost;
                        }
		    			afterBlockReplace(getWorldObj(), location, result.block, result.amount);
		    		}
		    	}
                actions--;
    		}
	    	
	    	// Auto-drain the inner tank
	    	if(!getTank().isEmpty()) {
				for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
					TileEntity tile = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
					if(!getTank().isEmpty() && tile instanceof IFluidHandler) {
						IFluidHandler handler = (IFluidHandler) tile;
						FluidStack fluidStack = new FluidStack(getTank().getFluidType(), Math.min(MB_RATE, getTank().getFluidAmount()));
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
			regionIterator = new RegionIterator(new Location(xCoord, yCoord, zCoord), (hasEfficiency() ? OFFSET_EFFICIENCY : OFFSET), true);
		}
		return regionIterator.next();
	}

}
