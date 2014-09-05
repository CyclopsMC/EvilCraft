package evilcraft.entities.tileentities;

import net.minecraft.world.World;
import evilcraft.blocks.InvisibleRedstoneBlock;
import evilcraft.core.entities.tileentitites.EvilCraftTileEntity;
import evilcraft.items.RedstoneGrenadeConfig;

/**
 * Tile for the {@link InvisibleRedstoneBlock}.
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
    public void updateEntity() {
        if (worldObj.getTotalWorldTime() - tickCreated >= TICK_DESTRUCTION_THRESHOLD) {
        	worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        	if(RedstoneGrenadeConfig.dropAfterUsage) {
        		InvisibleRedstoneBlock.getInstance().dropBlockAsItem(worldObj, xCoord, yCoord, zCoord, 0, 0);
        	}
        }
    }
}
