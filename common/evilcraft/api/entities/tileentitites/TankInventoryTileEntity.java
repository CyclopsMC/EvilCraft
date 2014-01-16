package evilcraft.api.entities.tileentitites;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import evilcraft.api.fluids.SingleUseTank;

/**
 * A TileEntity that has an inventory and a tank that can accept fluids or only one type of fluid.
 * @author rubensworks
 *
 */
public abstract class TankInventoryTileEntity extends InventoryTileEntity implements IFluidHandler {
    
    protected SingleUseTank tank;

    /**
     * Make new tile with a tank that can accept anything and an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     */
    public TankInventoryTileEntity(int inventorySize, String inventoryName, int tankSize, String tankName) {
        super(inventorySize, inventoryName);
        tank = new SingleUseTank(tankName, tankSize, this);
    }
    
    /**
     * Make new tile with a tank that can accept only one fluid and an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     * @param acceptedFluid Type of Fluid to accept.
     */
    public TankInventoryTileEntity(int inventorySize, String inventoryName, int tankSize, String tankName, Fluid acceptedFluid) {
        this(inventorySize, inventoryName, tankSize, tankName);
        tank.setAcceptedFluid(acceptedFluid);
    }
    
    /**
     * Get the internal tank
     * @return The internal SingleUseTank
     */
    public SingleUseTank getTank() {
        return tank;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        tank.readFromNBT(data);
    }

    @Override
    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        tank.writeToNBT(data);
    }
    
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int filled = tank.fill(resource, doFill);
        return filled;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource,
            boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(tank.getFluid()))
            return null;
        FluidStack drained = drain(from, resource.amount, doDrain);
        return drained;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack drained = tank.drain(maxDrain, doDrain);
        return drained;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return tank.getAcceptedFluid() == null || tank.getAcceptedFluid().equals(fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return tank.getAcceptedFluid() == null || tank.getAcceptedFluid().equals(fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        FluidTankInfo[] info = new FluidTankInfo[1];
        info[0] = tank.getInfo();
        return info;
    }

}
