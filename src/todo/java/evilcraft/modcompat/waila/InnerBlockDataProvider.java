package evilcraft.modcompat.waila;

import evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocks;
import evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocksExtended;
import evilcraft.core.tileentity.InnerBlocksTileEntity;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
<<<<<<< HEAD
=======
>>>>>>> origin/master

import java.util.List;

/**
 * Waila data provider for blocks with inner blocks.
 * @author rubensworks
 *
 */
public class InnerBlockDataProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if(accessor.getBlock() instanceof ConfigurableBlockWithInnerBlocks && config.getConfig(Waila.getInnerBlockConfigID())) {
            ConfigurableBlockWithInnerBlocks block = (ConfigurableBlockWithInnerBlocks) accessor.getBlock();
            return accessor.getStack();
        }
        if(accessor.getBlock() instanceof ConfigurableBlockWithInnerBlocksExtended && config.getConfig(Waila.getInnerBlockConfigID())) {
        	InnerBlocksTileEntity tile = (InnerBlocksTileEntity) accessor.getTileEntity();
            return accessor.getStack();
        }
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack,
            List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack,
            List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack,
            List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        return currenttip;
    }
    
}
