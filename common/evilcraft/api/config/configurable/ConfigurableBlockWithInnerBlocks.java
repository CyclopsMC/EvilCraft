package evilcraft.api.config.configurable;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.ExtendedConfig;

/**
 * A block that is based on inner blocks that are stored in the meta data.
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockWithInnerBlocks extends ConfigurableBlock implements IInformationProvider{

    // No more than 16 different innerblocks allowed!
    protected final Block[] INNER_BLOCKS;
    
    public ConfigurableBlockWithInnerBlocks(ExtendedConfig eConfig, Material material) {
        super(eConfig, material);
        INNER_BLOCKS = makeInnerBlockList();
    }
    
    protected abstract Block[] makeInnerBlockList();
    
    @SuppressWarnings("unchecked")
    @Override
    public void getSubBlocks(int id, CreativeTabs creativeTabs, List list) {
        for (int j = 0; j < INNER_BLOCKS.length; ++j) {
            list.add(new ItemStack(id, 1, j));
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        return INNER_BLOCKS[meta].getBlockTextureFromSide(side);
    }
    
    @Override
    public int idDropped(int meta, Random random, int zero) {
        return getBlockFromMetadata(meta).blockID;
    }
    
    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(getBlockFromMetadata(meta));
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return "Block: "+EnumChatFormatting.ITALIC+getBlockFromMetadata(itemStack.getItemDamage()).getLocalizedName();
    }
    
    /**
     * Get the metadata for the given (inner) block id
     * @param blockID The id to search a stained version for
     * @return metadata for this block or -1 if none can be found.
     */
    public int getMetadataFromBlockID(int blockID) {
        for(int i = 0; i < INNER_BLOCKS.length; i++) {
            if(INNER_BLOCKS[i].blockID == blockID)
                return i;
        }
        return -1;
    }

    /**
     * Get the Block from the given (inner) block metadata
     * @param blockMetadata metadata for the inner block.
     * @return The Block for the given metadata or the last available Block if the metadata was out of range.
     */
    public Block getBlockFromMetadata(int blockMetadata) {
        return INNER_BLOCKS[Math.min(INNER_BLOCKS.length - 1, blockMetadata)];
    }
    
}
