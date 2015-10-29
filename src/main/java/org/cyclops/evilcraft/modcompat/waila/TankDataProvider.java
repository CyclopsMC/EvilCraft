package org.cyclops.evilcraft.modcompat.waila;

import mcp.mobius.waila.api.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.item.DamageIndicatedItemComponent;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.Reference;

/**
 * Waila data provider for tanks.
 * @author rubensworks
 *
 */
public class TankDataProvider implements IWailaDataProvider {

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
        if(accessor.getTileEntity() instanceof TankInventoryTileEntity && config.getConfig(Waila.getTankConfigID(), true)) {
            TankInventoryTileEntity tile = (TankInventoryTileEntity) accessor.getTileEntity();
            if(tile.getTank().isEmpty()) {
                currenttip.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("general." + Reference.MOD_ID + ".info.empty"));
            } else {
                FluidStack fluidStack = tile.getTank().getFluid();
                currenttip.add(DamageIndicatedItemComponent.getInfo(fluidStack, fluidStack.amount, tile.getTank().getCapacity()));
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
