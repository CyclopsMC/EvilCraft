package evilcraft.tileentity;

import net.minecraft.item.ItemStack;
import evilcraft.core.tileentity.TankInventoryTileEntity;

/**
 * Tile Entity for the dark tank.
 * @author rubensworks
 *
 */
public class TileDarkTank extends TankInventoryTileEntity {
	
	/**
	 * The base capacity of the tank.
	 */
	public static final int BASE_CAPACITY = 4000;
	/**
	 * The NBT tag name of the tank.
	 */
	public static final String NBT_TAG_TANK = "darkTank";

	/**
	 * Make a new instance.
	 */
	public TileDarkTank() {
		super(0, "inventory", BASE_CAPACITY, NBT_TAG_TANK);
	}
	
	@Override
	public boolean isItemValidForSlot(int side, ItemStack item) {
		return false;
	}

	@Override
    public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}
	
	/**
	 * Get the filled ratio of this tank.
	 * @return The ratio.
	 */
	public double getFillRatio() {
		return ((double) getTank().getFluidAmount()) / (double) getTank().getCapacity();
	}

}
