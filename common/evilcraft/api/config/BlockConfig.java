package evilcraft.api.config;

import evilcraft.api.item.ItemBlockMetadata;
import evilcraft.api.render.AlphaItemRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;

public abstract class BlockConfig extends ExtendedConfig<BlockConfig> {

    public BlockConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends Block> element) {
        super(defaultId, name, namedId, comment, element);
    }
    
    /**
     * Override this method if this block has subtypes.
     * @return if the target block has subtypes.
     */
    public boolean hasSubTypes() {
        return false;
    }
    
    /**
     * If hasSubTypes() returns true this method can be overwritten to define another ItemBlock class
     * @return the ItemBlock class to use for the target block.
     */
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockMetadata.class;
    }
    
    /**
     * If the Configurable is registered in the OreDictionary, use this name to identify it.
     * @return the name this Configurable is registered with in the OreDictionary.
     */
    public String getOreDictionaryId() {
        return null;
    }
    
    @Override
    public void onRegistered() {
        if(getOreDictionaryId() != null) {
            OreDictionary.registerOre(getOreDictionaryId(), new ItemStack((Block)this.getSubInstance()));
        }
    }

}
