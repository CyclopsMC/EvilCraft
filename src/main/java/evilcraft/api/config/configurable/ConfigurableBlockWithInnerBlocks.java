package evilcraft.api.config.configurable;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
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
    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableBlockWithInnerBlocks(ExtendedConfig eConfig, Material material) {
        super(eConfig, material);
        INNER_BLOCKS = makeInnerBlockList();
    }
    
    /**
     * This should be implemented and return a list of innerblocks, they all must refer to
     * non-null block instanced to work correctly.
     * @return The list of innerblocks
     */
    protected abstract Block[] makeInnerBlockList();
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for (int j = 0; j < INNER_BLOCKS.length; ++j) {
            list.add(new ItemStack(item, 1, j));
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
    	return getBlockFromMetadata(meta).getBlockTextureFromSide(side);
    }
    
    @Override
    public Item getItemDropped(int meta, Random random, int zero) {
        return getBlockFromMetadata(meta).getItemDropped(meta, random, zero);
    }
    
    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(getBlockFromMetadata(meta));
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return "Block: "+EnumChatFormatting.ITALIC+getBlockFromMetadata(itemStack.getItemDamage()).getLocalizedName();
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        
    }
    
    /**
     * Get the metadata for the given (inner) block id
     * @param block The block to search a stained version for
     * @return metadata for this block or -1 if none can be found.
     */
    public int getMetadataFromBlock(Block block) {
        for(int i = 0; i < INNER_BLOCKS.length; i++) {
            if(INNER_BLOCKS[i] == block)
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
    
    /**
     * Get the amount of innerblocks
     * @return amount of innerblocks
     */
    public int getInnerBlocks() {
        return INNER_BLOCKS.length;
    }
    
    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return getBlockFromMetadata(meta).getBlockHardness(world, x, y, z);
    }
    
    @Override
    public int damageDropped(int meta) {
        return meta;
    }
    
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
        return getBlockFromMetadata(meta).canHarvestBlock(player, 0);
    }
    
    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return getBlockFromMetadata(meta).getPlayerRelativeBlockHardness(player, world, x, y, z);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return getBlockFromMetadata(world.getBlockMetadata(x, y, z)).colorMultiplier(world, x, y, z);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return getBlockFromMetadata(meta).getRenderColor(0);
    }
    
}
