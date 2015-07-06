package evilcraft.core.config.configurable;

import evilcraft.core.helper.L10NHelpers;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.List;

/**
 * Item that can hold ExtendedConfigs which places blocks on use.
 * @author rubensworks
 *
 */
public abstract class ConfigurableItemForBlock extends ItemReed implements IConfigurable {
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * Make a new item instance.
     * @param eConfig Config for this blockState.
     * @param block The blockState to place.
     */
    @SuppressWarnings({ "rawtypes" })
    protected ConfigurableItemForBlock(ExtendedConfig eConfig, Block block) {
        super(block);
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }
    
    @SuppressWarnings("rawtypes")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        L10NHelpers.addOptionalInfo(list, getUnlocalizedName());
    }

}
