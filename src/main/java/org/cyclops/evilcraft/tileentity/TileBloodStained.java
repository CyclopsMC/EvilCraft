package org.cyclops.evilcraft.tileentity;

import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodStained;

/**
 * Tile for the {@link BlockBloodStained}.
 * @author rubensworks
 *
 */
public class TileBloodStained extends CyclopsTileEntity {
    
	@NBTPersist
	private Integer amount = 0;

	public TileBloodStained() {
		super(RegistryEntries.TILE_ENTITY_BLOOD_STAINED);
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
		this.amount += amount;
		markDirty();
	}
    
}
