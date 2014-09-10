package evilcraft.core.config.configurable;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.L10NHelpers;

/**
 * Item food that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableItemFood extends ItemFood implements IConfigurable{
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param healAmount Amount of health to regen.
     * @param saturationModifier The modifier for the saturation.
     */
    @SuppressWarnings({ "rawtypes" })
    protected ConfigurableItemFood(ExtendedConfig eConfig, int healAmount, float saturationModifier, boolean isWolfsFavoriteMeat) {
        super(healAmount, saturationModifier, isWolfsFavoriteMeat);
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    @Override
    public String getIconString() {
        return Reference.MOD_ID+":"+eConfig.getNamedId();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }
    
    @SuppressWarnings("rawtypes")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        L10NHelpers.addOptionalInfo(list, getUnlocalizedName());
    }

}
