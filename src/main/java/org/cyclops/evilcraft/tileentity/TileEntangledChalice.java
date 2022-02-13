package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;

import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.ITickingTile;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.TickingTileComponent;

/**
 * Tile Entity for the entangled chalice.
 * @author rubensworks
 *
 */
public class TileEntangledChalice extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {
	
	/**
	 * The base capacity of the tank.
	 */
	public static final int BASE_CAPACITY = 4000;

	@Delegate
	private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

	private final WorldSharedTank tank;

	public TileEntangledChalice() {
		super(RegistryEntries.TILE_ENTITY_ENTANGLED_CHALICE);
		tank = new WorldSharedTank(BASE_CAPACITY);
		addCapabilityInternal(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, LazyOptional.of(this::getTank));
	}

	public WorldSharedTank getTank() {
		return tank;
	}

	/**
	 * Get the filled ratio of this tank.
	 * @return The ratio.
	 */
	public double getFillRatio() {
		int prev = ((WorldSharedTank) getTank()).getPreviousAmount();
		float alpha = (float) (WorldSharedTankCache.getInstance().getTickOffset()) / (float) WorldSharedTankCache.INTERPOLATION_TICK_OFFSET;
		double interpolatedAmount = prev * (1.0F - alpha) + getTank().getFluidAmount() * alpha;
		return Math.min(1.0D, (interpolatedAmount) / (double) getTank().getCapacity());
	}
	
	@Override
	protected void updateTileEntity() {
		super.updateTileEntity();
        ((WorldSharedTank) getTank()).resetPreviousFluid(); // Optimization for map look-ups in the shared tank.
	}

	/**
	 * @return The unique key of the internal tank.
	 */
	public String getWorldTankId() {
		return ((WorldSharedTank) getTank()).getTankID();
	}

	/**
	 * Set the unique key of the internal tank.
	 * @param tankId The new id.
	 */
	public void setWorldTankId(String tankId) {
		((WorldSharedTank) getTank()).setTankID(tankId);
	}

	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);
		tank.readFromNBT(tag, "tank");
	}

	@Override
	public CompoundNBT save(CompoundNBT tag) {
		tank.writeToNBT(tag, "tank");
		return super.save(tag);
	}

}
