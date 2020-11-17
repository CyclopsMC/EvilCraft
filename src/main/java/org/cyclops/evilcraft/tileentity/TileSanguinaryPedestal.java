package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodStain;
import org.cyclops.evilcraft.block.BlockSanguinaryPedestal;
import org.cyclops.evilcraft.block.BlockSanguinaryPedestalConfig;
import org.cyclops.evilcraft.core.algorithm.RegionIterator;
import org.cyclops.evilcraft.core.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.network.packet.SanguinaryPedestalBlockReplacePacket;

/**
 * Tile for the {@link BlockSanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class TileSanguinaryPedestal extends TankInventoryTileEntity implements CyclopsTileEntity.ITickingTile {
    
    private static final int MB_RATE = 100;
    private static final int TANK_BUCKETS = 10;
    private static final int OFFSET = 2;
    private static final int OFFSET_EFFICIENCY = 4;
    private static final int ACTIONS_PER_TICK_EFFICIENCY = 5;

	@Delegate
	private final ITickingTile tickingTileComponent = new TickingTileComponent(this);
    
    private RegionIterator regionIterator;

    public TileSanguinaryPedestal() {
        super(RegistryEntries.TILE_ENTITY_SANGUINARY_PEDESTAL, 0, 1, FluidHelpers.BUCKET_VOLUME * TANK_BUCKETS, RegistryEntries.FLUID_BLOOD);
    }

	@Override
	protected void addItemHandlerCapabilities() {
		// Don't expose inventory
	}

	public void fillWithPotentialBonus(FluidStack fluidStack) {
        if(hasEfficiency() && !fluidStack.isEmpty()) {
			fluidStack.setAmount((int) (fluidStack.getAmount() * BlockSanguinaryPedestalConfig.efficiencyBoost));
        }
        getTank().fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
    }
    
    protected void afterBlockReplace(World world, BlockPos location, BlockState block, int amount) {
    	// NOTE: this is only called server-side, so make sure to send packets where needed.
    	
    	// Fill tank
    	if(!getTank().isFull()) {
			fillWithPotentialBonus(new FluidStack(RegistryEntries.FLUID_BLOOD, amount));
		}

		EvilCraft._instance.getPacketHandler().sendToAllAround(new SanguinaryPedestalBlockReplacePacket(location, block),
				LocationHelpers.createTargetPointFromLocation(world, location, SanguinaryPedestalBlockReplacePacket.RANGE));
    }

    protected boolean hasEfficiency() {
        return ((BlockSanguinaryPedestal) getBlockState().getBlock()).getTier() == 1;
    }
    
    @Override
    public void updateTileEntity() {
    	super.updateTileEntity();

        if(!getWorld().isRemote()) {
            int actions = hasEfficiency() ? ACTIONS_PER_TICK_EFFICIENCY : 1;
	    	// Drain next blockState in tick
    		while(!getTank().isFull() && actions > 0) {
		    	BlockPos location = getNextLocation();
		    	Block block = getWorld().getBlockState(location).getBlock();
		    	if(block instanceof BlockBloodStain) {
					// TODO: reimplement like redstone dust block
		    		/*BloodStainedBlock.UnstainResult result = BloodStainedBlock.getInstance().unstainBlock(getWorld(),
		    				location, getTank().getCapacity() - getTank().getFluidAmount());
		    		if(result.amount > 0) {
		    			afterBlockReplace(getWorld(), location, result.block.getBlock(), result.amount);
		    		}*/
		    	}
                actions--;
    		}
	    	
	    	// Auto-drain the inner tank
	    	if(!getTank().isEmpty()) {
				for(Direction direction : Direction.values()) {
					IFluidHandler handler = TileHelpers.getCapability(getWorld(), getPos().offset(direction),
							direction.getOpposite(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
					if(!getTank().isEmpty() && handler != null) {
						FluidStack fluidStack = new FluidStack(getTank().getFluid(), Math.min(MB_RATE, getTank().getFluidAmount()));
						if(handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) > 0) {
							int filled = handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
							getTank().drain(filled, IFluidHandler.FluidAction.EXECUTE);
						}
					}
				}
			}
    	}
    	
    }
	
	private BlockPos getNextLocation() {
		if(regionIterator == null) {
			regionIterator = new RegionIterator(getPos(), (hasEfficiency() ? OFFSET_EFFICIENCY : OFFSET), true);
		}
		return regionIterator.next();
	}

}
