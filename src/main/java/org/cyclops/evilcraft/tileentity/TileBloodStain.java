package org.cyclops.evilcraft.tileentity;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodStain;

import javax.annotation.Nonnull;

/**
 * Tile for the {@link BlockBloodStain}.
 * @author rubensworks
 *
 */
public class TileBloodStain extends CyclopsTileEntity {

	public static final int CAPACITY = 5000;
    
	@NBTPersist
	private Integer amount = 0;

	public TileBloodStain() {
		super(RegistryEntries.TILE_ENTITY_BLOOD_STAIN);
		addCapabilityInternal(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, LazyOptional.of(() -> new FluidHandler(this)));
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to add
	 */
	public void addAmount(int amount) {
		this.amount = Math.min(CAPACITY, Math.max(0, this.amount + amount));
		if (this.amount == 0) {
			getWorld().removeBlock(getPos(), false);
		}
		markDirty();
	}

	public static class FluidHandler implements IFluidHandler {
		private final TileBloodStain tile;

		public FluidHandler(TileBloodStain tile) {
			this.tile = tile;
		}

		@Override
		public int getTanks() {
			return 1;
		}

		@Nonnull
		@Override
		public FluidStack getFluidInTank(int tank) {
			return new FluidStack(RegistryEntries.FLUID_BLOOD, tile.getAmount());
		}

		@Override
		public int getTankCapacity(int tank) {
			return CAPACITY;
		}

		@Override
		public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
			return tank == 0 && stack.getFluid() == RegistryEntries.FLUID_BLOOD;
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return 0;
		}

		@Nonnull
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			if (resource.getFluid() == RegistryEntries.FLUID_BLOOD) {
				return drain(resource.getAmount(), action);
			}
			return FluidStack.EMPTY;
		}

		@Nonnull
		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			maxDrain = Math.min(tile.getAmount(), maxDrain);
			FluidStack drained = new FluidStack(RegistryEntries.FLUID_BLOOD, maxDrain);
			if (action.execute()) {
				tile.addAmount(-maxDrain);
			}
			return drained;
		}
	}
    
}
