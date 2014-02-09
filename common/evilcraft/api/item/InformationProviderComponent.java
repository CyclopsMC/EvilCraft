package evilcraft.api.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;

/**
 * A component that can be used for {@link IInformationProvider} implementations.
 * @author rubensworks
 *
 */
public class InformationProviderComponent {
    
    private boolean hasInfo = false;
    private Block block = null;
    
    /**
     * Make a new instance.
     * @param block The block for which the component is used.
     */
    public InformationProviderComponent(Block block) {
        setBlock(block);
    }
    
    /**
     * Add information to the given list for the given item.
     * @param itemStack The {@link ItemStack} to add info for.
     * @param entityPlayer The player that will see the info.
     * @param list The info list where the info will be added.
     * @param par4 No idea...
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        if(hasInfo) {
            list.add(IInformationProvider.BLOCK_PREFIX+((IInformationProvider) block).getInfo(itemStack));
            ((IInformationProvider) block).provideInformation(itemStack, entityPlayer, list, par4);
        }
    }

    /**
     * If the block that uses this component implements {@link IInformationProvider}.
     * @return If the block provides info.
     */
    public boolean isHasInfo() {
        return hasInfo;
    }

    /**
     * Get the block that uses this component.
     * @return The block.
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Set the block that uses this component.
     * @param block The block.
     */
    public void setBlock(Block block) {
        this.block = block;
        if(block instanceof IInformationProvider)
            this.hasInfo = true;
    }
    
}
