package evilcraft.items;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
import evilcraft.blocks.LargeDoor;

/**
 * Item for the {@link LargeDoor}.
 * @author rubensworks
 *
 */
public class LargeDoorItem extends ConfigurableItem {
    
    private static LargeDoorItem _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new LargeDoorItem(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static LargeDoorItem getInstance() {
        return _instance;
    }

    private LargeDoorItem(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.maxStackSize = 1;
    }
    
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float coordX, float coordY, float coordZ) {
        if (side != 1) {
            return false;
        } else {
            ++y;
            Block block = LargeDoor.getInstance();

            if (player.canPlayerEdit(x, y, z, side, itemStack)
                    && player.canPlayerEdit(x, y + 1, z, side, itemStack)
                    && player.canPlayerEdit(x, y + 2, z, side, itemStack)) {
                
                if (!block.canPlaceBlockAt(world, x, y, z)) {
                    return false;
                } else {
                    int i1 = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                    placeDoorBlock(world, x, y, z, i1, block);
                    --itemStack.stackSize;
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    /**
     * Place a door block.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param side The side placed at.
     * @param block The block ID to place.
     */
    public static void placeDoorBlock(World world, int x, int y, int z, int side, Block block) {
        byte b0 = 0;
        byte b1 = 0;

        if (side == 0) {
            b1 = 1;
        }

        if (side == 1) {
            b0 = -1;
        }

        if (side == 2) {
            b1 = -1;
        }

        if (side == 3) {
            b0 = 1;
        }

        /*int i1 = (world.isBlockNormalCube(x - b0, y, z - b1) ? 1 : 0)
                + (world.isBlockNormalCube(x - b0, y + 1, z - b1) ? 1 : 0)
                + (world.isBlockNormalCube(x - b0, y + 2, z - b1) ? 1 : 0);
        int j1 = (world.isBlockNormalCube(x + b0, y, z + b1) ? 1 : 0)
                + (world.isBlockNormalCube(x + b0, y + 1, z + b1) ? 1 : 0)
                + (world.isBlockNormalCube(x + b0, y + 2, z + b1) ? 1 : 0);
        boolean flag = world.getBlockId(x - b0, y, z - b1) == block.blockID
                || world.getBlockId(x - b0, y + 1, z - b1) == block.blockID
                || world.getBlockId(x - b0, y + 2, z - b1) == block.blockID;
        boolean flag1 = world.getBlockId(x + b0, y, z + b1) == block.blockID
                || world.getBlockId(x + b0, y + 1, z + b1) == block.blockID
                || world.getBlockId(x + b0, y + 2, z + b1) == block.blockID;
        boolean flag2 = flag && !flag1 || j1 > i1;

        world.setBlock(x, y, z, block.blockID, side, 2);
        world.setBlock(x, y + 1, z, block.blockID, 8 | (flag2 ? 1 : 0), 2);
        world.setBlock(x, y + 2, z, block.blockID, 16 | (flag2 ? 1 : 0), 2);
        world.notifyBlocksOfNeighborChange(x, y, z, block.blockID);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, block.blockID);
        world.notifyBlocksOfNeighborChange(x, y + 2, z, block.blockID);*/
    }

}
