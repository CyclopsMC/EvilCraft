package evilcraft.core.config.extendedconfig;

import evilcraft.core.client.render.item.AlphaRenderItem;
import evilcraft.core.config.ConfigurableType;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.helper.MinecraftHelpers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Config for items.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class ItemConfig extends ExtendedConfig<ItemConfig> {

    /**
     * Make a new instance.
     * @param enabled If this should is enabled.O
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public ItemConfig(boolean enabled, String namedId, String comment, Class<? extends Item> element) {
        super(enabled, namedId, comment, element);
    }

    @Override
    protected IConfigurable initSubInstance() {
        return this.getElement() == null ? new ConfigurableItem(this) : super.initSubInstance();
    }
    
    @Override
	public String getUnlocalizedName() {
		return "items." + getNamedId();
	}

    @Override
    public String getFullUnlocalizedName() {
        return "item." + getUnlocalizedName() + ".name";
    }
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.ITEM;
	}
    
    /**
     * If the IConfigurable is registered in the OreDictionary, use this name to identify it.
     * @return the name this IConfigurable is registered with in the OreDictionary.
     */
    public String getOreDictionaryId() {
        return null;
    }
    
    /**
     * If this item should be rendered with a blended alpha channel, thereby using the AlphaRenderItem.
     * @return If it should be alpha blended.
     */
    public boolean blendAlpha() {
        return false;
    }
    
    /**
     * Get the casted instance of the item.
     * @return The item.
     */
    public Item getItemInstance() {
    	return (Item) super.getSubInstance();
    }

    /**
     * All the possible meta values of this item.
     * @return Array of all the available meta values.
     */
    public int[] getSubTypes() {
        return new int[]{0};
    }
    
    @Override
    public void onRegistered() {
    	if(isEnabled()) {
	        if(blendAlpha() && MinecraftHelpers.isClientSide())
	            MinecraftForgeClient.registerItemRenderer(this.getItemInstance(), new AlphaRenderItem());
	        
	        if(getOreDictionaryId() != null) {
	            OreDictionary.registerOre(getOreDictionaryId(), new ItemStack(this.getItemInstance()));
	        }
    	}
    }

}
