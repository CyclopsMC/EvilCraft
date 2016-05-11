package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.block.DarkTank;

import java.util.List;

/**
 * Tile Entity for the dark tank.
 * @author rubensworks
 *
 */
public class TileDarkTank extends TankInventoryTileEntity implements CyclopsTileEntity.ITickingTile {
	
	/**
	 * The base capacity of the tank.
	 */
	public static final int BASE_CAPACITY = 16000;
	/**
	 * The NBT tag name of the tank.
	 */
	public static final String NBT_TAG_TANK = "darkTank";

	@Delegate
	private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

	/**
	 * Make a new instance.
	 */
	public TileDarkTank() {
		super(0, "inventory", BASE_CAPACITY, NBT_TAG_TANK);
		this.setSendUpdateOnTankChanged(true);
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack item) {
		return false;
	}

	@Override
    public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}
	
	/**
	 * Get the filled ratio of this tank.
	 * @return The ratio.
	 */
	public double getFillRatio() {
		return Math.min(1.0D, ((double) getTank().getFluidAmount()) / (double) getTank().getCapacity());
	}
	
	protected boolean shouldAutoDrain() {
		return BlockHelpers.getSafeBlockStateProperty(worldObj.getBlockState(getPos()), DarkTank.DRAINING, false);
	}
	
	@Override
	protected void updateTileEntity() {
		if(!getWorld().isRemote && !getTank().isEmpty() && shouldAutoDrain()) {
			EnumFacing down = EnumFacing.DOWN;
            TileEntity tile = worldObj.getTileEntity(getPos().offset(down));
			if(tile instanceof IFluidHandler) {
				IFluidHandler handler = (IFluidHandler) tile;
				FluidStack fluidStack = new FluidStack(getTank().getFluidType(),
                        Math.min(GeneralConfig.mbFlowRate, getTank().getFluidAmount()));
				if(handler.fill(down.getOpposite(), fluidStack, false) > 0) {
					int filled = handler.fill(down.getOpposite(), fluidStack, true);
					drain(filled, true);
				}
			} else {
				// Try to fill fluid container items below
				List<Entity> entities = worldObj.getEntitiesWithinAABB(Entity.class,
						new AxisAlignedBB(getPos().offset(down), getPos().offset(down).add(1, 1, 1)),
						EntitySelectors.IS_ALIVE);
				for(Entity entity : entities) {
					if(!getTank().isEmpty() && entity instanceof EntityItem) {
						EntityItem item = (EntityItem) entity;
						if (item.getEntityItem() != null && item.getEntityItem().getItem() instanceof IFluidContainerItem &&
								item.getEntityItem().stackSize == 1) {
							ItemStack itemStack = item.getEntityItem().copy();
							ItemStack fillItemStack;
							if((fillItemStack = fill(itemStack)) != null) {
								item.setEntityItemStack(fillItemStack);
							}
						}
					} else if(entity instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) entity;
						PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
						while(!getTank().isEmpty() && it.hasNext()) {
							ItemStack itemStack = it.next();
							ItemStack fillItemStack;
							if(itemStack != null && itemStack.getItem() instanceof IFluidContainerItem &&
									(fillItemStack = fill(itemStack)) != null) {
								it.replace(fillItemStack);
							}
						}
					}
				}
			}
		}
	}

	protected ItemStack fill(ItemStack itemStack) {
		ItemStack fillItemStack = itemStack.copy();
		IFluidContainerItem container = (IFluidContainerItem) itemStack.getItem();
		FluidStack fluidStack = new FluidStack(getTank().getFluidType(),
				Math.min(GeneralConfig.mbFlowRate, getTank().getFluidAmount()));
		if (container.fill(fillItemStack, fluidStack, false) > 0) {
			int filled = container.fill(fillItemStack, fluidStack, true);
			drain(filled, true);
			return fillItemStack;
		}
		return null;
	}
	
	@Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
		return from != EnumFacing.DOWN && super.canFill(from, fluid);
	}

}
