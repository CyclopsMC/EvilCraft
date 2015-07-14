package evilcraft.core.config.configurable;

import evilcraft.client.render.model.ModelInnerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;

import java.util.List;
import java.util.Random;

/**
 * A blockState that is based on inner blocks that are stored in the meta data.
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockWithInnerBlocks extends ConfigurableBlock implements IInformationProvider{

    // No more than 16 different innerblocks allowed!
    protected final IBlockState[] INNER_BLOCKS;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableBlockWithInnerBlocks(ExtendedConfig eConfig, Material material) {
        super(eConfig, material);
        INNER_BLOCKS = makeInnerBlockList();
    }
    
    /**
     * This should be implemented and return a list of innerblocks, they all must refer to
     * non-null blockState instanced to work correctly.
     * @return The list of innerblocks
     */
    protected abstract IBlockState[] makeInnerBlockList();
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for (int j = 0; j < INNER_BLOCKS.length; ++j) {
            list.add(new ItemStack(item, 1, j));
        }
    }
    
    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        return getBlockFromState(blockState).getBlock().getItemDropped(getBlockFromState(blockState), random, zero);
    }
    
    @Override
    protected ItemStack createStackedBlock(IBlockState blockState) {
        return new ItemStack(getBlockFromState(blockState).getBlock());
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return L10NHelpers.localize("tile.blocks.evilcraft.innerBlock.info", EnumChatFormatting.ITALIC+ getBlockFromMeta(itemStack.getItemDamage()).getBlock().getLocalizedName());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        
    }
    
    /**
     * Get the metadata for the given (inner) blockState id
     * @param block The blockState to search a stained version for
     * @return metadata for this blockState or -1 if none can be found.
     */
    public int getMetadataFromBlock(Block block) {
        for(int i = 0; i < INNER_BLOCKS.length; i++) {
            if(INNER_BLOCKS[i].getBlock() == block)
                return i;
        }
        return -1;
    }

    protected abstract PropertyInteger getMetaProperty();

    /**
     * Get the Block from the given (inner) blockState metadata
     * @param blockState state for the inner blockState.
     * @return The Block for the given metadata or the last available Block if the metadata was out of range.
     */
    public IBlockState getBlockFromState(IBlockState blockState) {
        return getBlockFromMeta((Integer) blockState.getValue(getMetaProperty()));
    }

    /**
     * Get the Block from the given (inner) blockState metadata
     * @param blockMeta meta for the inner blockState.
     * @return The Block for the given metadata or the last available Block if the metadata was out of range.
     */
    public IBlockState getBlockFromMeta(int blockMeta) {
        return INNER_BLOCKS[Math.min(INNER_BLOCKS.length - 1, blockMeta)];
    }
    
    /**
     * Get the amount of innerblocks
     * @return amount of innerblocks
     */
    public int getInnerBlocks() {
        return INNER_BLOCKS.length;
    }
    
    @Override
    public float getBlockHardness(World world, BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos);
        return getBlockFromState(blockState).getBlock().getBlockHardness(world, blockPos);
    }
    
    @Override
    public int damageDropped(IBlockState blockState) {
        return (Integer) blockState.getValue(getMetaProperty());
    }
    
    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos blockPos, EntityPlayer player) {
        return getBlockFromState(world.getBlockState(blockPos)).getBlock().canHarvestBlock(world, blockPos, player);
    }
    
    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos);
        return getBlockFromState(blockState).getBlock().getPlayerRelativeBlockHardness(player, world, blockPos);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, BlockPos blockPos, int renderPass) {
        return getBlockFromState(world.getBlockState(blockPos)).getBlock().colorMultiplier(world, blockPos, renderPass);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(IBlockState blockState) {
        return getBlockFromState(blockState).getBlock().getRenderColor(blockState);
    }

    @Override
    public boolean hasDynamicModel() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IBakedModel createDynamicModel() {
        return new ModelInnerBlock(this);
    }
    
}