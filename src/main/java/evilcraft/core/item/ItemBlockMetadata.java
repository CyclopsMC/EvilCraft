package evilcraft.core.item;

import org.cyclops.cyclopscore.item.IInformationProvider;
import evilcraft.core.block.IBlockRarityProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.L10NHelpers;

import java.util.List;

/**
 * An extended {@link ItemBlock} that will automatically add information to the blockState
 * item if that blockState implements {@link IInformationProvider}.
 * @author rubensworks
 *
 */
public class ItemBlockMetadata extends ItemBlock {
    
    protected InformationProviderComponent informationProvider;
    protected IBlockRarityProvider rarityProvider = null;

    /**
     * Make a new instance.
     * @param block The blockState instance.
     */
    public ItemBlockMetadata(Block block) {
        super(block);
        informationProvider = new InformationProviderComponent(block);
        if(block instanceof IBlockRarityProvider) {
            rarityProvider = (IBlockRarityProvider) block;
        }
    }
    
    @SuppressWarnings("rawtypes")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        L10NHelpers.addOptionalInfo(list, getUnlocalizedName());
    	informationProvider.addInformation(itemStack, entityPlayer, list, par4);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        if(rarityProvider != null) {
            return rarityProvider.getRarity(itemStack);
        }
        return super.getRarity(itemStack);
    }

}
