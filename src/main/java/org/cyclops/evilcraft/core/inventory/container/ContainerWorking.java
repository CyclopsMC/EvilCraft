package org.cyclops.evilcraft.core.inventory.container;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A container for a working tile entity.
 * @author rubensworks
 *
 * @param <T> The {@link WorkingTileEntity} class.
 */
public abstract class ContainerWorking<T extends TileWorking<T, ?>> extends ContainerInventoryTickingTank<T> {

    private final List<Supplier<Boolean>> variablesUpgradeSlotEnabled;

    public ContainerWorking(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory,
                            IInventory inventory, Optional<T> tileSupplier, int tickers, int upgradeSlots) {
        super(type, id, playerInventory, inventory, tileSupplier, tickers);
        this.variablesUpgradeSlotEnabled = Lists.newArrayList();
        for (int i = 0; i < upgradeSlots; i++) {
            int finalI = i;
            this.variablesUpgradeSlotEnabled.add(registerSyncedVariable(Boolean.class, () -> getTileSupplier().get()
                    .isUpgradeSlotEnabled(getTileSupplier().get().getBasicInventorySize() + finalI)));
        }
    }

    public boolean isUpgradeSlotEnabled(int slot) {
        return this.variablesUpgradeSlotEnabled.get(slot).get();
    }

    public abstract WorkingTileEntity.IMetadata getTileWorkingMetadata();
	
}
