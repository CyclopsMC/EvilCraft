package evilcraft.api.config.configurable;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

/**
 * Item food that can hold ExtendedConfigs
 * @author Ruben Taelman
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected ConfigurableItemBucket(ExtendedConfig eConfig, int blockID) {
        super(eConfig.ID, blockID);
        eConfig.ID = this.itemID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
        setContainerItem(Item.bucketEmpty);
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
    public void registerIcons(IconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }
    
    @Override
    public boolean isEntity() {
        return false;
    }
    
}
