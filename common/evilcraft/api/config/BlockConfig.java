package evilcraft.api.config;

import evilcraft.api.item.ItemBlockMetadata;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

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

}
