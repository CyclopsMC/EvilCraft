package evilcraft.api.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemBlockNBT extends ItemBlockExtended {
    
    public ItemBlockNBT(int blockID, Block block) {
        super(blockID, block);
        this.setMaxStackSize(1);
    }
    
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (tile != null && stack.stackTagCompound != null) {
                tile.readFromNBT(stack.stackTagCompound);
            }

            return true;
        }

        return false;
    }

}
