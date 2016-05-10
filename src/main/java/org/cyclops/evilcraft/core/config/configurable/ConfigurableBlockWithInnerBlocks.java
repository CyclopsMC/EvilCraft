package org.cyclops.evilcraft.core.config.configurable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.client.render.model.ModelInnerBlock;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * A blockState that is based on inner blocks that are stored in the meta data.
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockWithInnerBlocks extends ConfigurableBlock implements IInformationProvider {

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
        return L10NHelpers.localize("tile.blocks.evilcraft.innerBlock.info", TextFormatting.ITALIC+ getBlockFromMeta(itemStack.getItemDamage()).getBlock().getLocalizedName());
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
        if(blockState.getBlock() != this) {
            return INNER_BLOCKS[0];
        }
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
    public float getBlockHardness(IBlockState blockState, World world, BlockPos blockPos) {
        return getBlockFromState(blockState).getBlock().getBlockHardness(blockState, world, blockPos);
    }
    
    @Override
    public int damageDropped(IBlockState blockState) {
        return (Integer) blockState.getValue(getMetaProperty());
    }
    
    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos blockPos, EntityPlayer player) {
        return true;
    }
    
    @Override
    public float getPlayerRelativeBlockHardness(IBlockState blockState, EntityPlayer player, World world, BlockPos blockPos) {
        return getBlockFromState(blockState).getBlock().getPlayerRelativeBlockHardness(blockState, player, world, blockPos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState blockState, World worldObj, RayTraceResult target, EffectRenderer effectRenderer) {
        BlockPos pos = target.getBlockPos();
        RenderHelpers.addBlockHitEffects(effectRenderer, worldObj, getBlockFromState(blockState), pos, target.sideHit);
        return true;
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

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColorHandler() {
        return new BlockColor();
    }

    @SideOnly(Side.CLIENT)
    public static class BlockColor implements IBlockColor {
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockState blockState, IBlockAccess world, BlockPos blockPos, int renderPass) {
            if(blockPos != null) {
                ConfigurableBlockWithInnerBlocks thisBlock = (ConfigurableBlockWithInnerBlocks) blockState.getBlock();
                IBlockState blockStateInner = thisBlock.getBlockFromState(world.getBlockState(blockPos));
                Block block = blockStateInner.getBlock();
                if (block instanceof IBlockColor) {
                    return ((IBlockColor) block).colorMultiplier(blockStateInner, world, blockPos, renderPass);
                }
            }
            return -1;
        }
    }
}