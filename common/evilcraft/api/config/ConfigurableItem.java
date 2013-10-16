package evilcraft.api.config;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.EvilCraftTab;
import evilcraft.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

/**
 * Item that can hold ExtendedConfigs
 * @author Ruben Taelman
 *
 */
public abstract class ConfigurableItem extends Item implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.ITEM;
    
    protected ConfigurableItem(ExtendedConfig eConfig) {
        super(eConfig.ID);
        eConfig.ID = this.itemID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
    }

    // Set a configuration for this item
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    public String getUniqueName() {
        return "items."+eConfig.NAMEDID;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(Reference.MOD_ID+":"+eConfig.NAMEDID);
    }
    
    public boolean isEntity() {
        return false;
    }

}
