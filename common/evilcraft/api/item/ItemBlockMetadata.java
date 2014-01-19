package evilcraft.api.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockMetadata extends ItemBlockWithMetadata{
    
    protected boolean hasInfo = false;
    protected Block block = null;

    public ItemBlockMetadata(int metaData, Block block) {
        super(metaData, block);
        this.block = block;
        if(block instanceof IInformationProvider)
            hasInfo = true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        if(hasInfo)
            list.add(IInformationProvider.BLOCK_PREFIX+((IInformationProvider) block).getInfo(itemStack));
    }

}
