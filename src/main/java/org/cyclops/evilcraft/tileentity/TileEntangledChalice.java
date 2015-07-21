package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;

/**
 * Tile Entity for the entangled chalice.
 * @author rubensworks
 *
 */
public class TileEntangledChalice extends TankInventoryTileEntity implements CyclopsTileEntity.ITickingTile {
	
	/**
	 * The base capacity of the tank.
	 */
	public static final int BASE_CAPACITY = 4000;
	/**
	 * The NBT tag name of the tank.
	 */
	public static final String NBT_TAG_TANK = "entangledChalice";

	@Delegate
	private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

	/**
	 * Make a new instance.
	 */
	public TileEntangledChalice() {
		super(0, "inventory", BASE_CAPACITY, NBT_TAG_TANK);
		this.setSendUpdateOnTankChanged(true);
	}
	
	@Override
	protected SingleUseTank newTank(String tankName, int tankSize) {
    	return new WorldSharedTank(tankName, tankSize, this);
    }
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack item) {
		return false;
	}

	@Override
    public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
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

}
