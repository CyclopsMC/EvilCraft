package evilcraft.api.config;

import net.minecraft.item.Item;

public abstract class ItemConfig extends ExtendedConfig<ItemConfig> {

    public ItemConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends Item> element) {
        super(defaultId, name, namedId, comment, element);
    }
    
    /**
     * If the Configurable is registered in the OreDictionary, use this name to identify it.
     * @return the name this Configurable is registered with in the OreDictionary.
     */
    public String getOreDictionaryId() {
        return null;
    }

}
