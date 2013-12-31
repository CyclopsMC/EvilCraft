package evilcraft.items;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.Helpers;
import evilcraft.api.config.ConfigurableItem;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.blocks.LargeDoor;
import evilcraft.entities.monster.Werewolf;
import evilcraft.entities.monster.WerewolfConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LargeDoorItem extends ConfigurableItem {
    
    private static LargeDoorItem _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new LargeDoorItem(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static LargeDoorItem getInstance() {
        return _instance;
    }

    private LargeDoorItem(ExtendedConfig eConfig) {
        super(eConfig);
        this.maxStackSize = 1;
    }
    
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (par7 != 1) {
            return false;
        } else {
            ++par5;
            Block block = LargeDoor.getInstance();

            if (player.canPlayerEdit(par4, par5, par6, par7, itemStack)
                    && player.canPlayerEdit(par4, par5 + 1, par6, par7, itemStack)
                    && player.canPlayerEdit(par4, par5 + 2, par6, par7, itemStack)) {
                
                if (!block.canPlaceBlockAt(par3World, par4, par5, par6)) {
                    return false;
                } else {
                    int i1 = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                    placeDoorBlock(par3World, par4, par5, par6, i1, block);
                    --itemStack.stackSize;
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public static void placeDoorBlock(World world, int x, int y, int z, int side, Block par5Block) {
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

        int i1 = (world.isBlockNormalCube(x - b0, y, z - b1) ? 1 : 0)
                + (world.isBlockNormalCube(x - b0, y + 1, z - b1) ? 1 : 0)
                + (world.isBlockNormalCube(x - b0, y + 2, z - b1) ? 1 : 0);
        int j1 = (world.isBlockNormalCube(x + b0, y, z + b1) ? 1 : 0)
                + (world.isBlockNormalCube(x + b0, y + 1, z + b1) ? 1 : 0)
                + (world.isBlockNormalCube(x + b0, y + 2, z + b1) ? 1 : 0);
        boolean flag = world.getBlockId(x - b0, y, z - b1) == par5Block.blockID
                || world.getBlockId(x - b0, y + 1, z - b1) == par5Block.blockID
                || world.getBlockId(x - b0, y + 2, z - b1) == par5Block.blockID;
        boolean flag1 = world.getBlockId(x + b0, y, z + b1) == par5Block.blockID
                || world.getBlockId(x + b0, y + 1, z + b1) == par5Block.blockID
                || world.getBlockId(x + b0, y + 2, z + b1) == par5Block.blockID;
        boolean flag2 = flag && !flag1 || j1 > i1;

        world.setBlock(x, y, z, par5Block.blockID, side, 2);
        world.setBlock(x, y + 1, z, par5Block.blockID, 8 | (flag2 ? 1 : 0), 2);
        world.setBlock(x, y + 2, z, par5Block.blockID, 16 | (flag2 ? 1 : 0), 2);
        world.notifyBlocksOfNeighborChange(x, y, z, par5Block.blockID);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, par5Block.blockID);
        world.notifyBlocksOfNeighborChange(x, y + 2, z, par5Block.blockID);
    }

}
