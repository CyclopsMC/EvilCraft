package evilcraft.modcompat.waila;

import evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocks;
import evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocksExtended;
import evilcraft.core.tileentity.InnerBlocksTileEntity;
import mcp.mobius.waila.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Waila data provider for blocks with inner blocks.
 * @author rubensworks
 *
 */
public class InnerBlockDataProvider implements IWailaDataProvider {

    protected ItemStack getItemStack(IBlockState blockState) {
        return new ItemStack(Item.getItemFromBlock(blockState.getBlock()), 1, blockState.getBlock().getMetaFromState(blockState));
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if(accessor.getBlock() instanceof ConfigurableBlockWithInnerBlocks && config.getConfig(Waila.getInnerBlockConfigID())) {
            ConfigurableBlockWithInnerBlocks block = (ConfigurableBlockWithInnerBlocks) accessor.getBlock();
            return getItemStack(block.getBlockFromMeta(accessor.getMetadata()));
        }
        if(accessor.getBlock() instanceof ConfigurableBlockWithInnerBlocksExtended && config.getConfig(Waila.getInnerBlockConfigID())) {
        	InnerBlocksTileEntity tile = (InnerBlocksTileEntity) accessor.getTileEntity();
            return getItemStack(tile.getInnerBlockState());
        }
        return null;
    }

    @Override
    public ITaggedList.ITipList getWailaHead(ItemStack itemStack, ITaggedList.ITipList currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public ITaggedList.ITipList getWailaTail(ItemStack itemStack, ITaggedList.ITipList currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public ITaggedList.ITipList getWailaBody(ItemStack itemStack, ITaggedList.ITipList currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(TileEntity te, NBTTagCompound tag, IWailaDataAccessorServer accessor) {
        return tag;
    }

}
