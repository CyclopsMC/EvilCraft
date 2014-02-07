package evilcraft.api.config;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import evilcraft.api.item.ItemBlockExtended;

public abstract class BlockConfig extends ExtendedConfig<BlockConfig> {

    public BlockConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends Block> element) {
        super(defaultId, name, namedId, comment, element);
    }
    
    /**
     * If hasSubTypes() returns true this method can be overwritten to define another ItemBlock class
     * @return the ItemBlock class to use for the target block.
     */
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockExtended.class;
    }
    
    /**
     * If the Configurable is registered in the OreDictionary, use this name to identify it.
     * @return the name this Configurable is registered with in the OreDictionary.
     */
    public String getOreDictionaryId() {
        return null;
    }
    
    /**
     * If this block should enable Forge Multiparts and BC facades.
     * @return If that should be enabled for this block.
     */
    public boolean isMultipartEnabled() {
        return false;
    }

}
