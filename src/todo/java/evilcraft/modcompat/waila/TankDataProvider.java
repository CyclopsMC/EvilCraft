package evilcraft.modcompat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Waila data provider for tanks.
 * @author rubensworks
 *
 */
public class TankDataProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        // TODO Auto-generated method stub
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
        if(accessor.getTileEntity() instanceof TankInventoryTileEntity && config.getConfig(Waila.getTankConfigID(), true)) {
            TankInventoryTileEntity tile = (TankInventoryTileEntity) accessor.getTileEntity();
            if(tile.getTank().isEmpty()) {
                currenttip.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("general.info.empty"));
            } else {
                FluidStack fluidStack = tile.getTank().getFluid();
                String name = fluidStack.getFluid().getLocalizedName(fluidStack);
                currenttip.add(name + ": " + fluidStack.amount + " / " + tile.getTank().getCapacity());
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
