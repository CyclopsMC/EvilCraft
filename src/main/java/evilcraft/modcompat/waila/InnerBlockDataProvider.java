package evilcraft.modcompat.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import evilcraft.api.config.configurable.ConfigurableBlockWithInnerBlocks;

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
            return new ItemStack(block.getBlockFromMetadata(accessor.getMetadata()));
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
