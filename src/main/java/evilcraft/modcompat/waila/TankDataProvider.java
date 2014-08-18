package evilcraft.modcompat.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.api.entities.tileentitites.TankInventoryTileEntity;
import evilcraft.api.helpers.L10NHelpers;

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
                currenttip.add(EnumChatFormatting.ITALIC + "Empty");
            } else {
                FluidStack fluidStack = tile.getTank().getFluid();
                String name = L10NHelpers.localize("fluid.fluids." + FluidRegistry.getFluidName(fluidStack));
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
    
}
