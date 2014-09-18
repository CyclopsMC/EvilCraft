package evilcraft.tileentity;

import net.minecraft.world.World;
import evilcraft.block.InvisibleRedstoneBlock;
import evilcraft.core.tileentity.EvilCraftTileEntity;
import evilcraft.item.RedstoneGrenadeConfig;

/**
 * Tile for the {@link InvisibleRedstoneBlock}.
 * TODO: remove TE and implement with metadata with altered block ticktime (see pressure plate)
 * @author rubensworks
 *
 */
public class TileInvisibleRedstoneBlock extends EvilCraftTileEntity {
    
    // Destroy redstone block after 1 redstone tick (= 2 game ticks)
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
    	super.updateTileEntity();
    	
        if (worldObj.getTotalWorldTime() - tickCreated >= TICK_DESTRUCTION_THRESHOLD) {
        	worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        	if(RedstoneGrenadeConfig.dropAfterUsage) {
        		InvisibleRedstoneBlock.getInstance().dropBlockAsItem(worldObj, xCoord, yCoord, zCoord, 0, 0);
        	}
        }
    }
}
