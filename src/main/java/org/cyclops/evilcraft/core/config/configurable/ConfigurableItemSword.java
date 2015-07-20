package org.cyclops.evilcraft.core.config.configurable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;

import java.util.List;

/**
 * Item pickaxe that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableItemSword extends ItemSword implements IConfigurable {
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    protected boolean canPickUp = true;
    
    /**
     * Make a new bucket instance.
     * @param eConfig Config for this blockState.
     * @param material The material of the tool.
     */
    @SuppressWarnings({ "rawtypes" })
    protected ConfigurableItemSword(ExtendedConfig eConfig, Item.ToolMaterial material) {
        super(material);
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
