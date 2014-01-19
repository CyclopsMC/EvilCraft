package evilcraft.api.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;

public class ItemBlockFluidContainer extends ItemBlockNBT {

    protected boolean hasInfo = false;
    protected Block block = null;
    
    public ItemBlockFluidContainer(int blockID, Block block) {
        super(blockID, block);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        if(hasInfo)
            list.add(IInformationProvider.BLOCK_PREFIX+((IInformationProvider) block).getInfo(itemStack));
    }

}
