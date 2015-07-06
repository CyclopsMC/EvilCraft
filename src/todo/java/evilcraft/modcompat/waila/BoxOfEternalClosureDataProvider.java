package evilcraft.modcompat.waila;

import evilcraft.core.helper.L10NHelpers;
import evilcraft.entity.monster.VengeanceSpirit;
import evilcraft.tileentity.TileBoxOfEternalClosure;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

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
                currenttip.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("general.info.empty"));
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

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        return tag;
    }

}
