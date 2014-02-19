package evilcraft.api.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;

/**
 * An extended {@link ItemBlock} that will automatically add information to the block
 * item if that block implements {@link IInformationProvider}.
 * @author rubensworks
 *
 */
public class ItemBlockExtended extends ItemBlock {

    protected InformationProviderComponent informationProvider;

    /**
     * Make a new instance.
     * @param block The block instance.
     */
    public ItemBlockExtended(Block block) {
    	super(block);
        informationProvider = new InformationProviderComponent(block);
    }
    
    @SuppressWarnings("rawtypes")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        informationProvider.addInformation(itemStack, entityPlayer, list, par4);
    }
    
}
