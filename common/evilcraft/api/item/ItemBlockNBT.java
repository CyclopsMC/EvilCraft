package evilcraft.api.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemBlockNBT extends ItemBlock{

    protected boolean hasInfo = false;
    protected Block block = null;
    
    public ItemBlockNBT(int blockID, Block block) {
        super(blockID);
        if(block instanceof IInformationProvider)
            hasInfo = true;
        this.block = block;
        this.setMaxStackSize(1);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        if(hasInfo)
            list.add(IInformationProvider.BLOCK_PREFIX+((IInformationProvider) block).getInfo(itemStack));
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
