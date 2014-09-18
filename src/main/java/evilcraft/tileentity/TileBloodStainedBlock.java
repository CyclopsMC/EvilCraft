package evilcraft.tileentity;

import evilcraft.block.BloodStainedBlock;
import evilcraft.core.tileentity.InnerBlocksTileEntity;
import evilcraft.core.tileentity.NBTPersist;

/**
 * Tile for the {@link BloodStainedBlock}.
 * @author rubensworks
 *
 */
public class TileBloodStainedBlock extends InnerBlocksTileEntity {
    
	@NBTPersist
	private Integer amount = 0;

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
		this.amount += amount;
		sendUpdate();
	}
	
	@Override
	protected void onDeprecationTrigger() {
		addAmount(500);
	}
    
}
