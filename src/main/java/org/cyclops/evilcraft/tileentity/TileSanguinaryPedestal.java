package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.fluid.FluidHandlerWrapper;
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

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.ITickingTile;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.TickingTileComponent;

/**
 * Tile for the {@link BlockSanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class TileSanguinaryPedestal extends TankInventoryTileEntity implements CyclopsTileEntity.ITickingTile {
    
    private static final int MB_RATE = 100;
    public static final int TANK_BUCKETS = 10;
    private static final int OFFSET = 2;
    private static final int OFFSET_EFFICIENCY = 4;
    private static final int ACTIONS_PER_TICK_EFFICIENCY = 5;

	@Delegate
	private final ITickingTile tickingTileComponent = new TickingTileComponent(this);
	private final IFluidHandler bonusFluidHandler;
    
    private RegionIterator regionIterator;

    public TileSanguinaryPedestal() {
        super(RegistryEntries.TILE_ENTITY_SANGUINARY_PEDESTAL, 0, 1, FluidHelpers.BUCKET_VOLUME * TANK_BUCKETS, RegistryEntries.FLUID_BLOOD);
        this.bonusFluidHandler = new FluidHandlerWrapper(getTank()) {
			@Override
			public int fill(FluidStack resource, FluidAction action) {
				if (hasEfficiency() && !resource.isEmpty()) {
					resource.setAmount((int) (resource.getAmount() * BlockSanguinaryPedestalConfig.efficiencyBoost));
				}
				return super.fill(resource, action);
			}
		};
    }

	/**
	 * @return The inner tank of this pedestal that when filled can have a bonus applied.
	 */
	public IFluidHandler getBonusFluidHandler() {
		return bonusFluidHandler;
	}

	@Override
	protected void addItemHandlerCapabilities() {
		// Don't expose inventory
	}
    
    protected void afterBlockReplace(World world, BlockPos location) {
    	// NOTE: this is only called server-side, so make sure to send packets where needed.
		EvilCraft._instance.getPacketHandler().sendToAllAround(new SanguinaryPedestalBlockReplacePacket(location.getX(), location.getY(), location.getZ()),
				LocationHelpers.createTargetPointFromLocation(world, location, SanguinaryPedestalBlockReplacePacket.RANGE));
    }

    protected boolean hasEfficiency() {
        return ((BlockSanguinaryPedestal) getBlockState().getBlock()).getTier() == 1;
    }
    
    @Override
    public void updateTileEntity() {
    	super.updateTileEntity();

        if(!getLevel().isClientSide()) {
            int actions = hasEfficiency() ? ACTIONS_PER_TICK_EFFICIENCY : 1;
	    	// Drain next blockState in tick
    		while(!getTank().isFull() && actions > 0) {
		    	BlockPos location = getNextLocation();
		    	Block block = getLevel().getBlockState(location).getBlock();
		    	if(block instanceof BlockBloodStain) {
					TileHelpers.getCapability(getLevel(), location, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
							.ifPresent((source) -> {
								FluidStack moved = FluidUtil.tryFluidTransfer(getBonusFluidHandler(), source, Integer.MAX_VALUE, true);
								if (!moved.isEmpty()) {
									afterBlockReplace(getLevel(), location);
								}
							});
		    	}
                actions--;
    		}
	    	
	    	// Auto-drain the inner tank
	    	if(!getTank().isEmpty()) {
				for(Direction direction : Direction.values()) {
					TileHelpers.getCapability(getLevel(), getBlockPos().relative(direction),
							direction.getOpposite(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
							.ifPresent(handler -> {
								if(!getTank().isEmpty()) {
									FluidStack fluidStack = new FluidStack(getTank().getFluid(), Math.min(MB_RATE, getTank().getFluidAmount()));
									if(handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) > 0) {
										int filled = handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
										getTank().drain(filled, IFluidHandler.FluidAction.EXECUTE);
									}
								}
							});
				}
			}
    	}
    	
    }
	
	private BlockPos getNextLocation() {
		if(regionIterator == null) {
			regionIterator = new RegionIterator(getBlockPos(), (hasEfficiency() ? OFFSET_EFFICIENCY : OFFSET), true);
		}
		return regionIterator.next();
	}

}
