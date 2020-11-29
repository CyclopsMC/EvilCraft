package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
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
    
    private int destroyCountDown;

    public TileInvisibleRedstone() {
        super(RegistryEntries.TILE_ENTITY_INVISIBLE_REDSTONE);
        destroyCountDown = TICK_DESTRUCTION_THRESHOLD;
    }
    
    @Override
    public void updateTileEntity() {
        if (destroyCountDown-- <= 0) {
        	world.removeBlock(getPos(), false);
        	if(ItemRedstoneGrenadeConfig.dropAfterUsage) {
                ItemStackHelpers.spawnItemStack(world, getPos(), new ItemStack(RegistryEntries.ITEM_REDSTONE_GRENADE));
        	}
        }
    }
}
