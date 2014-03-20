package evilcraft.api.config.configurable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

/**
 * Item food that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableItemBucket extends ItemBucket implements Configurable{
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.ITEM;
    
    protected boolean canPickUp = true;
    
    /**
     * Make a new bucket instance.
     * @param eConfig Config for this block.
     * @param blockID The fluid block ID it can pick up.
     */
    @SuppressWarnings({ "rawtypes" })
    protected ConfigurableItemBucket(ExtendedConfig eConfig, Block block) {
        super(block);
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
        setContainerItem(Items.bucket);
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
