package evilcraft.api.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;

public class InformationProviderComponent {
    
    private boolean hasInfo = false;
    private Block block = null;
    
    public InformationProviderComponent(Block block) {
        setBlock(block);
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        if(hasInfo) {
            list.add(IInformationProvider.BLOCK_PREFIX+((IInformationProvider) block).getInfo(itemStack));
            ((IInformationProvider) block).provideInformation(itemStack, entityPlayer, list, par4);
        }
    }

    public boolean isHasInfo() {
        return hasInfo;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
        if(block instanceof IInformationProvider)
            this.hasInfo = true;
    }
    
}
