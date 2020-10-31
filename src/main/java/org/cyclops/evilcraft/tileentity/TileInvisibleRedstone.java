package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockInvisibleRedstone;
import org.cyclops.evilcraft.item.ItemRedstoneGrenadeConfig;

/**
 * Tile for the {@link BlockInvisibleRedstone}.
 * TODO: remove TE and implement with metadata with altered blockState ticktime (see pressure plate)
 * @author rubensworks
 *
 */
public class TileInvisibleRedstone extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {
    
    // Destroy redstone blockState after 1 redstone tick (= 2 game ticks)
    private static final int TICK_DESTRUCTION_THRESHOLD = 2;

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);
    
    private final long tickCreated;

    public TileInvisibleRedstone() {
        super(RegistryEntries.TILE_ENTITY_INVISIBLE_REDSTONE);
        tickCreated = world.getGameTime();
    }
    
    @Override
    public void updateTileEntity() {
        if (world.getGameTime() - tickCreated >= TICK_DESTRUCTION_THRESHOLD) {
        	world.removeBlock(getPos(), false);
        	if(ItemRedstoneGrenadeConfig.dropAfterUsage) {
        	    // TODO: reimplement with loot tables
                //getBlockState().getBlock().dropBlockAsItem(world, getPos(), world.getBlockState(getPos()), 0);
        	}
        }
    }
}
