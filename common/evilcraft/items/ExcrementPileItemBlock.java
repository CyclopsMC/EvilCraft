package evilcraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.blocks.ExcrementPile;
import evilcraft.blocks.ExcrementPileConfig;

public class ExcrementPileItemBlock extends ItemBlock {

    public ExcrementPileItemBlock(int par1)
    {
        super(par1);
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        int block = world.getBlockId(x, y, z);
        if (block == ExcrementPileConfig._instance.ID) {
            ExcrementPile.heightenPileAt(world, x, y, z);
            return true;
        }
        return super.onItemUse(par1ItemStack, par2EntityPlayer, world, x, y, z, side, par8, par9, par10);
    }

}
