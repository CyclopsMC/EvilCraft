package evilcraft.api.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockExtended extends ItemBlock {

    protected InformationProviderComponent informationProvider;

    public ItemBlockExtended(int blockID, Block block) {
        super(blockID);
        informationProvider = new InformationProviderComponent(block);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        informationProvider.addInformation(itemStack, entityPlayer, list, par4);
    }
    
}
