package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTankInventory;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Tile Entity for the dark tank.
 * @author rubensworks
 *
 */
public class BlockEntityDarkTank extends BlockEntityTankInventory {

    /**
     * The base capacity of the tank.
     */
    public static final int BASE_CAPACITY = 16000;

    @NBTPersist
    private boolean enabled;

    public BlockEntityDarkTank(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_DARK_TANK, blockPos, blockState, 0, 0, BASE_CAPACITY, null);
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

    @Nullable
    protected ItemStack fill(ItemStack itemStack) {
        IFluidHandlerItem container = itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
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

    public static class TickerServer extends BlockEntityTickerDelayed<BlockEntityDarkTank> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityDarkTank blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            if(!blockEntity.getTank().isEmpty() && blockEntity.isEnabled()) {
                Direction down = Direction.DOWN;
                IFluidHandler handler = BlockEntityHelpers.getCapability(level, pos.relative(down), down.getOpposite(),
                        ForgeCapabilities.FLUID_HANDLER).orElse(null);
                if(handler != null) {
                    FluidStack fluidStack = new FluidStack(blockEntity.getTank().getFluid(),
                            Math.min(GeneralConfig.mbFlowRate, blockEntity.getTank().getFluidAmount()));
                    if(handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) > 0) {
                        int filled = handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        blockEntity.getTank().drain(filled, IFluidHandler.FluidAction.EXECUTE);
                    }
                } else {
                    // Try to fill fluid container items below
                    List<Entity> entities = level.getEntitiesOfClass(Entity.class,
                            new AABB(pos.relative(down), pos.relative(down).offset(1, 1, 1)),
                            EntitySelector.ENTITY_STILL_ALIVE);
                    for(Entity entity : entities) {
                        if(!blockEntity.getTank().isEmpty() && entity instanceof ItemEntity) {
                            ItemEntity item = (ItemEntity) entity;
                            if (item.getItem() != null
                                    && item.getItem().getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() &&
                                    item.getItem().getCount() == 1) {
                                ItemStack itemStack = item.getItem().copy();
                                ItemStack fillItemStack;
                                if((fillItemStack = blockEntity.fill(itemStack)) != null) {
                                    item.setItem(fillItemStack);
                                }
                            }
                        } else if(entity instanceof Player) {
                            Player player = (Player) entity;
                            PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
                            while(!blockEntity.getTank().isEmpty() && it.hasNext()) {
                                ItemStack itemStack = it.next();
                                ItemStack fillItemStack;
                                if(!itemStack.isEmpty()
                                        && itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()
                                        && (fillItemStack = blockEntity.fill(itemStack)) != null) {
                                    it.replace(fillItemStack);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
