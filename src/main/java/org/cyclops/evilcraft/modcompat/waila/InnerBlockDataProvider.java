package org.cyclops.evilcraft.modcompat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocks;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocksExtended;
import org.cyclops.evilcraft.core.tileentity.InnerBlocksTileEntity;

import java.util.List;

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
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        return tag;
    }

}
