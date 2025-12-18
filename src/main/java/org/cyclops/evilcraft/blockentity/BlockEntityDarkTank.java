package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.inventory.ItemAccessItemLocation;
import org.cyclops.cyclopscore.inventory.ItemLocation;
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
        super(RegistryEntries.BLOCK_ENTITY_DARK_TANK.get(), blockPos, blockState, 0, 0, BASE_CAPACITY, null);
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
    protected boolean fill(ItemAccess itemAccess) {
        ResourceHandler<FluidResource> container = itemAccess.getCapability(Capabilities.Fluid.ITEM);
        return !IModHelpersNeoForge.get().getFluidHelpers().move(getTank(), container, Math.min(GeneralConfig.mbFlowRate, getTank().getFluidAmount()), null, false, false).isEmpty();
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
                ResourceHandler<FluidResource> handler = IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(level, pos.relative(down), down.getOpposite(),
                        net.neoforged.neoforge.capabilities.Capabilities.Fluid.BLOCK).orElse(null);
                if(handler != null) {
                    IModHelpersNeoForge.get().getFluidHelpers().move(blockEntity.getTank(), handler, Math.min(GeneralConfig.mbFlowRate, blockEntity.getTank().getFluidAmount()), null, false, false);
                } else {
                    // Try to fill fluid container items below
                    List<Entity> entities = level.getEntitiesOfClass(Entity.class,
                            new AABB(Vec3.atLowerCornerOf(pos.relative(down)), Vec3.atLowerCornerOf(pos.relative(down).offset(1, 1, 1))),
                            EntitySelector.ENTITY_STILL_ALIVE);
                    for(Entity entity : entities) {
                        if(!blockEntity.getTank().isEmpty() && entity instanceof ItemEntity) {
                            ItemEntity item = (ItemEntity) entity;
                            ItemAccess itemAccess = ItemAccess.forStack(item.getItem());
                            if (itemAccess.getCapability(net.neoforged.neoforge.capabilities.Capabilities.Fluid.ITEM) != null &&
                                    item.getItem().getCount() == 1) {
                                if(blockEntity.fill(itemAccess)) {
                                    item.setItem(itemAccess.getResource().toStack(itemAccess.getAmount()));
                                }
                            }
                        } else if(entity instanceof Player) {
                            Player player = (Player) entity;
                            PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
                            while(!blockEntity.getTank().isEmpty() && it.hasNext()) {
                                ItemLocation itemLocation = it.nextIndexed();
                                ItemAccess itemAccess = new ItemAccessItemLocation(player, itemLocation);
                                if(itemAccess.getAmount() > 0
                                        && itemAccess.getCapability(net.neoforged.neoforge.capabilities.Capabilities.Fluid.ITEM) != null) {
                                    blockEntity.fill(itemAccess);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
