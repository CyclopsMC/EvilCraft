package evilcraft.core.item;

import evilcraft.core.IInformationProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

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
     * @param block The blockState for which the component is used.
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
            if(((IInformationProvider) block).getInfo(itemStack) != null) {
                list.add(IInformationProvider.BLOCK_PREFIX + ((IInformationProvider) block).getInfo(itemStack));
            }
            ((IInformationProvider) block).provideInformation(itemStack, entityPlayer, list, par4);
        }
    }

    /**
     * If the blockState that uses this component implements {@link IInformationProvider}.
     * @return If the blockState provides info.
     */
    public boolean isHasInfo() {
        return hasInfo;
    }

    /**
     * Get the blockState that uses this component.
     * @return The blockState.
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Set the blockState that uses this component.
     * @param block The blockState.
     */
    public void setBlock(Block block) {
        this.block = block;
        if(block instanceof IInformationProvider)
            this.hasInfo = true;
    }
    
}
