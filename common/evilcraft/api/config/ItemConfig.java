package evilcraft.api.config;

import evilcraft.api.render.AlphaItemRenderer;
import evilcraft.items.DarkStick;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;

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
    
    /**
     * If this item should be rendered with a blended alpha channel, thereby using the AlphaItemRenderer.
     * @return If it should be alpha blended.
     */
    public boolean blendAlpha() {
        return false;
    }
    
    @Override
    public void onRegistered() {
        if(blendAlpha())
            MinecraftForgeClient.registerItemRenderer(this.ID, new AlphaItemRenderer());
        
        if(getOreDictionaryId() != null) {
            OreDictionary.registerOre(getOreDictionaryId(), new ItemStack((Item)this.getSubInstance()));
        }
    }

}
