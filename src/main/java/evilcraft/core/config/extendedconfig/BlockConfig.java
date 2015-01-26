package evilcraft.core.config.extendedconfig;

import evilcraft.core.config.ConfigurableType;
import evilcraft.core.item.ItemBlockExtended;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * Config for blocks.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BlockConfig extends ExtendedConfig<BlockConfig> {

    /**
     * Make a new instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public BlockConfig(boolean enabled, String namedId, String comment, Class<? extends Block> element) {
        super(enabled, namedId, comment, element);
    }

    @Override
	public String getUnlocalizedName() {
		return "blocks." + getNamedId();
	}

    @Override
    public String getFullUnlocalizedName() {
        return "tile." + getUnlocalizedName() + ".name";
    }
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.BLOCK;
	}
    
    /**
     * If hasSubTypes() returns true this method can be overwritten to define another ItemBlock class
     * @return the ItemBlock class to use for the target block.
     */
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockExtended.class;
    }
    
    /**
     * If the IConfigurable is registered in the OreDictionary, use this name to identify it.
     * @return the name this IConfigurable is registered with in the OreDictionary.
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

    /**
     * Get the casted instance of the block.
     * @return The block.
     */
    public Block getBlockInstance() {
        return (Block) super.getSubInstance();
    }

}
