package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDarkTank;
import org.cyclops.evilcraft.core.tileentity.TankInventoryTileEntity;

import javax.annotation.Nullable;
import java.util.List;

import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.ITickingTile;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.TickingTileComponent;

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

	@Delegate
	private final ITickingTile tickingTileComponent = new TickingTileComponent(this);
	@NBTPersist
	private boolean enabled;

	public TileDarkTank() {
		super(RegistryEntries.TILE_ENTITY_DARK_TANK, 0, 0, BASE_CAPACITY, null);
	}
	
	/**
	 * Get the filled ratio of this tank.
	 * @return The ratio.
	 */
	public double getFillRatio() {
		return Math.min(1.0D, ((double) getTank().getFluidAmount()) / (double) getTank().getCapacity());
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		sendUpdate();
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	protected void updateTileEntity() {
		if(!getLevel().isClientSide() && !getTank().isEmpty() && isEnabled()) {
			Direction down = Direction.DOWN;
			IFluidHandler handler = TileHelpers.getCapability(level, getBlockPos().relative(down), down.getOpposite(),
					CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
			if(handler != null) {
				FluidStack fluidStack = new FluidStack(getTank().getFluid(),
                        Math.min(GeneralConfig.mbFlowRate, getTank().getFluidAmount()));
				if(handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) > 0) {
					int filled = handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
					getTank().drain(filled, IFluidHandler.FluidAction.EXECUTE);
				}
			} else {
				// Try to fill fluid container items below
				List<Entity> entities = level.getEntitiesOfClass(Entity.class,
						new AxisAlignedBB(getBlockPos().relative(down), getBlockPos().relative(down).offset(1, 1, 1)),
						EntityPredicates.ENTITY_STILL_ALIVE);
				for(Entity entity : entities) {
					if(!getTank().isEmpty() && entity instanceof ItemEntity) {
						ItemEntity item = (ItemEntity) entity;
						if (item.getItem() != null
								&& item.getItem().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent() &&
								item.getItem().getCount() == 1) {
							ItemStack itemStack = item.getItem().copy();
							ItemStack fillItemStack;
							if((fillItemStack = fill(itemStack)) != null) {
								item.setItem(fillItemStack);
							}
						}
					} else if(entity instanceof PlayerEntity) {
						PlayerEntity player = (PlayerEntity) entity;
						PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
						while(!getTank().isEmpty() && it.hasNext()) {
							ItemStack itemStack = it.next();
							ItemStack fillItemStack;
							if(!itemStack.isEmpty()
									&& itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()
									&& (fillItemStack = fill(itemStack)) != null) {
								it.replace(fillItemStack);
							}
						}
					}
				}
			}
		}
	}

	@Nullable
	protected ItemStack fill(ItemStack itemStack) {
		IFluidHandlerItem container = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElse(null);
		FluidStack fluidStack = new FluidStack(getTank().getFluid(),
				Math.min(GeneralConfig.mbFlowRate, getTank().getFluidAmount()));
		if (container.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) > 0) {
			int filled = container.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
			getTank().drain(filled, IFluidHandler.FluidAction.EXECUTE);
			return container.getContainer();
		}
		return null;
	}

	@Override
	public void onTankChanged() {
		super.onTankChanged();
		sendUpdate();
	}
}
