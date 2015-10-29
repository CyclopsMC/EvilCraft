package org.cyclops.evilcraft.modcompat.waila;

import mcp.mobius.waila.api.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;

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
    public ITaggedList.ITipList getWailaHead(ItemStack itemStack, ITaggedList.ITipList currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public ITaggedList.ITipList getWailaBody(ItemStack itemStack, ITaggedList.ITipList currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if(accessor.getTileEntity() instanceof TileBoxOfEternalClosure
        		&& config.getConfig(Waila.getBoxOfEternalClosureConfigID(), true)) {
        	TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) accessor.getTileEntity();
            if(tile.getSpiritInstance() == null) {
                currenttip.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("general." + Reference.MOD_ID + ".info.empty"));
            } else {
                VengeanceSpirit spirit = ((VengeanceSpirit) tile.getSpiritInstance());
                String name = spirit.getLocalizedInnerEntityName();
                currenttip.add(name);
            }
        }
        return currenttip;
    }

    @Override
    public ITaggedList.ITipList getWailaTail(ItemStack itemStack, ITaggedList.ITipList currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(TileEntity te, NBTTagCompound tag, IWailaDataAccessorServer accessor) {
        return tag;
    }

}
