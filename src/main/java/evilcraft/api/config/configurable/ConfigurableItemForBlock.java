package evilcraft.api.config.configurable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemReed;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

/**
 * Item that can hold ExtendedConfigs which places blocks on use.
 * @author rubensworks
 *
 */
public abstract class ConfigurableItemForBlock extends ItemReed implements Configurable{
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.ITEM;
    
    /**
     * Make a new item instance.
     * @param eConfig Config for this block.
     * @param block The block to place.
     */
    @SuppressWarnings({ "rawtypes" })
    protected ConfigurableItemForBlock(ExtendedConfig eConfig, Block block) {
        super(block);
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    @Override
    public String getUniqueName() {
        return "items."+eConfig.NAMEDID;
    }
    
    @Override
    public String getIconString() {
        return Reference.MOD_ID+":"+eConfig.NAMEDID;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }
    
    @Override
    public boolean isEntity() {
        return false;
    }

}
