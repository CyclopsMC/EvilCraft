package evilcraft.entities.tileentities.tickaction.spiritfurnace;

import net.minecraft.item.ItemStack;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.entities.tileentities.TileSpiritFurnace;

/**
 * {@link ITickAction} that is able to cook boxes with spirits.
 * @author rubensworks
 *
 */
public class BoxCookTickAction implements ITickAction<TileSpiritFurnace> {
    
    protected final static int MB_PER_TICK = 100;
    
    @Override
    public boolean canTick(TileSpiritFurnace tile, ItemStack itemStack, int slot, int tick) {
        // TODO
    	return true;
    }

	@Override
	public void onTick(TileSpiritFurnace tile, ItemStack itemStack, int slot,
			int tick) {
		// TODO
	}

	@Override
	public int getRequiredTicks(TileSpiritFurnace tile, int slot) {
		// TODO
		return 0;
	}
    
}
