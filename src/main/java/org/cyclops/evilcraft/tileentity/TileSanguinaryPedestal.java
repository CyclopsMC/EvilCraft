package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.block.BloodStainedBlock;
import org.cyclops.evilcraft.block.PurifierConfig;
import org.cyclops.evilcraft.block.SanguinaryPedestal;
import org.cyclops.evilcraft.block.SanguinaryPedestalConfig;
import org.cyclops.evilcraft.core.algorithm.RegionIterator;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.network.packet.SanguinaryPedestalBlockReplacePacket;

/**
 * Tile for the {@link SanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class TileSanguinaryPedestal extends TankInventoryTileEntity implements CyclopsTileEntity.ITickingTile {
    
    /**
     * The fluid it uses.
     */
    public static final Fluid FLUID = Blood.getInstance();
    
    private static final int MB_RATE = 100;
    private static final int TANK_BUCKETS = 10;
    private static final int OFFSET = 2;
    private static final int OFFSET_EFFICIENCY = 4;
    private static final int ACTIONS_PER_TICK_EFFICIENCY = 5;

	@Delegate
	private final ITickingTile tickingTileComponent = new TickingTileComponent(this);
    
    private RegionIterator regionIterator;
    
    /**
     * Make a new instance.
     */
    public TileSanguinaryPedestal() {
        super(0, PurifierConfig._instance.getNamedId(), 1, FluidContainerRegistry.BUCKET_VOLUME * TANK_BUCKETS,
        		SanguinaryPedestalConfig._instance.getNamedId() + "tank", FLUID);
    }

    public void fillWithPotentialBonus(FluidStack fluidStack) {
        if(hasEfficiency() && fluidStack != null) {
            fluidStack.amount *= SanguinaryPedestalConfig.efficiencyBoost;
        }
        fill(fluidStack, true);
    }
    
    protected void afterBlockReplace(World world, BlockPos location, Block block, int amount) {
    	// NOTE: this is only called server-side, so make sure to send packets where needed.
    	
    	// Fill tank
    	if(!getTank().isFull()) {
			fillWithPotentialBonus(new FluidStack(FLUID, amount));
		}
    	
    	EvilCraft._instance.getPacketHandler().sendToServer(new SanguinaryPedestalBlockReplacePacket(location, block));
    }

    protected boolean hasEfficiency() {
        return getBlockMetadata() == 1;
    }
    
    @Override
    public void updateTileEntity() {
    	super.updateTileEntity();

        if(!getWorld().isRemote) {
            int actions = hasEfficiency() ? ACTIONS_PER_TICK_EFFICIENCY : 1;
	    	// Drain next blockState in tick
    		while(!getTank().isFull() && actions > 0) {
		    	BlockPos location = getNextLocation();
		    	Block block = getWorld().getBlockState(location).getBlock();
		    	if(block == BloodStainedBlock.getInstance()) {
		    		BloodStainedBlock.UnstainResult result = BloodStainedBlock.getInstance().unstainBlock(getWorld(),
		    				location, getTank().getCapacity() - getTank().getFluidAmount());
		    		if(result.amount > 0) {
		    			afterBlockReplace(getWorld(), location, result.block.getBlock(), result.amount);
		    		}
		    	}
                actions--;
    		}
	    	
	    	// Auto-drain the inner tank
	    	if(!getTank().isEmpty()) {
				for(EnumFacing direction : EnumFacing.VALUES) {
					TileEntity tile = worldObj.getTileEntity(getPos().offset(direction));
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
    public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}
	
	private BlockPos getNextLocation() {
		if(regionIterator == null) {
			regionIterator = new RegionIterator(getPos(), (hasEfficiency() ? OFFSET_EFFICIENCY : OFFSET), true);
		}
		return regionIterator.next();
	}

}
