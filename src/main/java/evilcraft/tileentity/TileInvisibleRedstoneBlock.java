package evilcraft.tileentity;

import evilcraft.block.InvisibleRedstoneBlock;
import evilcraft.item.RedstoneGrenadeConfig;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

/**
 * Tile for the {@link InvisibleRedstoneBlock}.
 * TODO: remove TE and implement with metadata with altered blockState ticktime (see pressure plate)
 * @author rubensworks
 *
 */
public class TileInvisibleRedstoneBlock extends CyclopsTileEntity implements IUpdatePlayerListBox, CyclopsTileEntity.ITickingTile {
    
    // Destroy redstone blockState after 1 redstone tick (= 2 game ticks)
    private static final int TICK_DESTRUCTION_THRESHOLD = 2;
    
    private final long tickCreated;
    
    /**
     * Make a new instance.
     * @param world The world.
     */
    public TileInvisibleRedstoneBlock(World world) {
        tickCreated = world.getTotalWorldTime();
    }
    
    @Override
    public void updateTileEntity() {
        if (worldObj.getTotalWorldTime() - tickCreated >= TICK_DESTRUCTION_THRESHOLD) {
        	worldObj.setBlockToAir(getPos());
        	if(RedstoneGrenadeConfig.dropAfterUsage) {
        		InvisibleRedstoneBlock.getInstance().dropBlockAsItem(worldObj, getPos(), worldObj.getBlockState(getPos()), 0);
        	}
        }
    }
}
