package evilcraft.modcompat.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import evilcraft.entities.monster.VengeanceSpirit;
import evilcraft.entities.tileentities.TileBoxOfEternalClosure;

/**
 * Waila data provider for the BOEC.
 * @author rubensworks
 *
 */
public class BoxOfEternalClosureDataProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
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
        if(accessor.getTileEntity() instanceof TileBoxOfEternalClosure
        		&& config.getConfig(Waila.getBoxOfEternalClosureConfigID(), true)) {
        	TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) accessor.getTileEntity();
            if(tile.getSpiritInstance() == null) {
                currenttip.add(EnumChatFormatting.ITALIC + "Empty");
            } else {
                String name = ((VengeanceSpirit) tile.getSpiritInstance()).getLocalizedInnerEntityName();
                currenttip.add(name);
            }
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack,
            List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        return currenttip;
    }
    
}
